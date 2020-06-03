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
        public ActionResult<List<Evento>> Get() =>
            _apuntateService.Get();

        [HttpGet("{id}", Name="GetEvento")]
        public ActionResult<Evento> Get(string id){
            var evento = _apuntateService.Get(id);
            if(evento == null){
                return NotFound();
            }
            return evento;
        }

        [HttpPost]
        public ActionResult<Evento> Create(Evento evento,
            [FromQuery] string email){
                Console.WriteLine(email);
            bool resul = isAlumno(email).Result;
            if(resul){
                Evento a = _apuntateService.Create(evento);
                actualizarTarea(a,email,VALOR_CONTROL_PENDIENTE);
                return CreatedAtRoute("GetEvento",new {id = evento.Id.ToString()},evento);
            } else {
                return NotFound();
            }
        }

        [HttpPut("{id}")]
        public IActionResult Update(
            string id, 
            Evento eventoMod,
            [FromQuery] string email){
            var evento = _apuntateService.Get(id);
            if(evento == null){
                return NotFound();
            }

            _apuntateService.Update(id,eventoMod);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(
            string id,
            [FromQuery] string email){
            var evento = _apuntateService.Get(id);

            if(evento == null){
                return NotFound();
            }

            _apuntateService.Remove(evento.Id);

            return NoContent();
        }

        private async Task<bool> isAlumno(string email){
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
            //COMPLETAR
        }

        private void actualizarTarea(Evento e, string email, int modificador){
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

        private string formarMensaje(Evento evento, string email, int modificador){

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
                    break;
                
                default:
                    break;
            }

            return mensaje;

        }

    }
}