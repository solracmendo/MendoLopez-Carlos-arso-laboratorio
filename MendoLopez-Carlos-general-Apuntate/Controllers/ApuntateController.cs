using Apuntate.Models;
using Apuntate.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using Microsoft.AspNetCore.Http;
using System;
//using Newtonsoft.Json;
using System.Text.Json;
using System.Threading.Tasks;
using System.IO;
using System.Net;
using System.Net.Http;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System.Text;
using System.Text.Json.Serialization;

namespace MendoLopez_Carlos_general_Apuntate.Controllers 
{
    [Route("api/[controller]")]
    [ApiController]

    public class ApuntateController : ControllerBase{

        public const int VALOR_CONTROL_PENDIENTE = 1;
        public const int VALOR_CONTROL_COMPLETADA = 2;
        public const int VALOR_CONTROL_ELIMINAR = 3;

    

        private readonly ApuntateService _apuntateService;
    
        public ApuntateController(ApuntateService apuntateService){
            _apuntateService = apuntateService;
        }

        [HttpGet]
        public ActionResult<List<Evento>> GetAllEventos() =>
            _apuntateService.Get(); //Obtener todos los eventos

        [HttpGet("{id}", Name="GetEvento")]
        public ActionResult<Evento> GetEvento(string id){ //Obtener informacion de un evento concreto
            if(id == null || id == ""){
                return BadRequest("El campo id debe contener informacion valida");
            }
            var evento = _apuntateService.Get(id);
            if(evento == null){
                return NotFound("La informacion del evento debe ser valida");
            }
            return evento;
        }

        [HttpPatch("{id}/{email}")]
        public IActionResult reservarTurno( //Usada por profesores y alumnos
            //Se reserva el turno de un evento
            string id,
            [FromQuery] string turno,
            string email)
            {
                if(id == null || id == ""){
                    return BadRequest("El id debe contener informacion valida");
                }

                if(turno == null || turno == ""){
                    return BadRequest("El turno debe contener informacion valida");
                }

                if(email == null || email ==""){
                    return BadRequest("El email debe contener informacion valida");
                }

                var evento = _apuntateService.Get(id);
                if(evento == null){
                    return NotFound("El evento no se ha encontrado");
                }

                foreach (Turno turn in evento.grupos){
                    if(turn.turno == turno){ //Si es el mismo,vemos si se ha asignado, y si no, se asigna.En caso contrario, a la lista de espera
                        if(turn.actual == null && !turn.listaEspera.Contains(email)){
                            turn.actual = email;
                        } else {
                            if(!turn.listaEspera.Contains(email)){
                                turn.listaEspera.Add(email);
                            } else {
                                return BadRequest("Un usuario no se puede registrar dos veces en el mismo turno");
                            }
                        }
                    }
                }

                //TRATAMIENTO CON COLAS
                actualizarTarea(evento,email,VALOR_CONTROL_COMPLETADA); //El usuario ha completado el registro
                _apuntateService.Update(evento.Id,evento);
                return NoContent();


            }

        [HttpPut("{id}/{email}")]
        public IActionResult cancelarTurno( //Usada por profesores y  alumnos
            //Se cancela la reserva de un turno
            string id,
            [FromQuery] string turno,
            string email)
            {
                if(id == null || id == ""){
                    return BadRequest("El id debe contener informacion valida");
                }

                if(turno == null || turno == ""){
                    return BadRequest("El turno debe contener informacion valida");
                }

                if(email == null || email ==""){
                    return BadRequest("El email debe contener informacion valida");
                }
                bool resul = isAlumno(email).Result;
                if(!resul){
                    return Unauthorized("Un profesor no puede cancelar un turno");
                }

                var evento = _apuntateService.Get(id);
                if(evento == null){
                    return NotFound("El evento no se ha encontrado");
                }

                foreach (Turno turn in evento.grupos){
                    if(turn.turno == turno){ //Si es el mismo,vemos si se ha asignado, y si no, se asigna.En caso contrario, a la lista de espera
                        if(turn.actual == email){
                            var nuevo = turn.listaEspera.ToArray()[0];
                            if(nuevo != null){
                                turn.actual = nuevo;
                                turn.listaEspera.Remove(nuevo);
                            } else {
                                turn.actual = null;
                            }
                        } else { //Si no esta en el turno principal, se borra de la lista de espera
                            turn.listaEspera.Remove(email);
                        }
                }
                }
                //TRATAMIENTO CON COLAS
                //actualizarTarea(evento,email,VALOR_CONTROL_COMPLETADA); //El usuario ha completado el registro
                _apuntateService.Update(evento.Id,evento);
                return NoContent();


            }

        [HttpPost]
        public ActionResult<Evento> CreateEvento(Evento evento,
            [FromQuery] string email){
            //Crear evento
                
            if(evento == null){
                return BadRequest("El evento debe contener informacion valida");
            }

            if(email == null || email == ""){
                return BadRequest("El email debe contener informacion valida");
            }


            bool resul = isAlumno(email).Result;
            if(!resul){
                Evento a = _apuntateService.Create(evento);
                actualizarTarea(a,email,VALOR_CONTROL_PENDIENTE);
                return CreatedAtRoute("GetEvento",new {id = evento.Id.ToString()},evento);
            } else {
                return Unauthorized("Un alumno no puede crear un evento");
            }

            
        }



        [HttpPut("{id}")]
        public IActionResult updateEvento(
            //Actualizar evento
            string id, 
            Evento eventoMod,
            [FromQuery] string email){

            if(id == null||id==""){
                return BadRequest("La informacion del id debe ser valida");
            }    

            if(eventoMod == null){
                return BadRequest("La informacion del evento debe ser valida");
            }

            if(email == null || email == ""){
                return BadRequest("La informacion del email debe ser valida");
            }
            var evento = _apuntateService.Get(id);
            if(evento == null){
                return NotFound("No se ha encontrado el evento");
            }
            bool resul = isAlumno(email).Result;
            if(resul){
                return Unauthorized("Un alumno no puede modificar un evento");
            }

            _apuntateService.Update(id,eventoMod);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult deleteEvento(
            //Eliminar evento
            string id,
            [FromQuery] string email){

            if(id == null || id == ""){
                return BadRequest("El id debe contener informacion valida");
            }

            if(email == null || email==""){
                return BadRequest("El email debe ser valido");
            }

            bool resul = isAlumno(email).Result;
            if(resul){
                return Unauthorized("Un alumno no puede eliminar un evento");
            }


            var evento = _apuntateService.Get(id);

            if(evento == null){
                return NotFound("No se ha encontrado el evento");
            }

            actualizarTarea(evento,email,VALOR_CONTROL_ELIMINAR);

            _apuntateService.Remove(evento.Id);



            return NoContent();
        }

        private async Task<bool> isAlumno(string email){ //Comprobar si un email pertenece a un alumno
            string requestUrl = "http://localhost:8086/api/usuarios/" + email;
            Console.WriteLine(requestUrl);
            using var client = new HttpClient();

            var response = await client.GetAsync(requestUrl);

            Console.WriteLine(response.StatusCode);
            if(response.StatusCode == HttpStatusCode.OK){
                return true;
            } else {
                return false;
            }

        }

        private void actualizarTarea(Evento e, string email, int modificador){ //Enviar mensajes a la cola de mensajes
            var factory = new ConnectionFactory() { Uri = new Uri("amqp://cikqdgnl:IpTo-xh4cN-UU2Aa0JX2nf1AN3chkLB4@squid.rmq.cloudamqp.com/cikqdgnl") };
            var connection = factory.CreateConnection();
            var channel = connection.CreateModel();

            channel.ExchangeDeclare(exchange: "arso-exchange", 
                                type: "direct", 
                                durable: true, 
                                autoDelete: false, 
                                arguments: null);

            channel.QueueDeclare(queue: "arso-queue",
                                 durable: true,
                                 exclusive: false,
                                 autoDelete: false,
                                 arguments: null);

            channel.QueueBind(queue: "arso-queue", 
                                exchange: "arso-exchange", 
                                routingKey: "arso-queue");

            string mensaje = formarMensaje(e,email,modificador);
            var body = Encoding.UTF8.GetBytes(mensaje);

            IBasicProperties props = channel.CreateBasicProperties();
            props.ContentType = "text/plain";

            channel.BasicPublish(exchange: "arso-exchange",
                                 routingKey: "arso-queue",
                                 basicProperties: props,
                                 body: body);

            channel.Close();
            connection.Close();
        }

        private string formarMensaje(Evento evento, string email, int modificador){ //Metodo de creacion para los mensajes de la cola

            string mensaje = null;
            switch (modificador)
            {
                case VALOR_CONTROL_PENDIENTE:
                    MensajePendiente pendiente = new MensajePendiente(modificador, evento.Titulo,evento.Id);
                    mensaje = JsonSerializer.Serialize(pendiente);
                    break;

                case VALOR_CONTROL_COMPLETADA:
                    MensajeCompletado completado = new MensajeCompletado(modificador,email,evento.Id);
                    mensaje = JsonSerializer.Serialize(completado);
                    break;

                case VALOR_CONTROL_ELIMINAR:
                    MensajeEliminar eliminar = new MensajeEliminar(modificador, evento.Id);
                    mensaje = JsonSerializer.Serialize(eliminar);
                    break;
                
                default:
                    break;
            }

            return mensaje;

        }

    }
}