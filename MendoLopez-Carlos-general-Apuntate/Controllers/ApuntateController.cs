using Apuntate.Models;
using Apuntate.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace MendoLopez_Carlos_general_Apuntate.Controllers 
{
    [Route("api/[controller]")]
    [ApiController]

    public class ApuntateController : ControllerBase{

    

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
        public ActionResult<Evento> Create(Evento evento){
            _apuntateService.Create(evento);
            return CreatedAtRoute("GetEvento",new {id = evento.Id.ToString()},evento);
        }

        [HttpPut("{id}")]
        public IActionResult Update(string id, Evento eventoMod){
            var evento = _apuntateService.Get(id);
            if(evento == null){
                return NotFound();
            }

            _apuntateService.Update(id,eventoMod);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(string id){
            var evento = _apuntateService.Get(id);

            if(evento == null){
                return NotFound();
            }

            _apuntateService.Remove(evento.Id);

            return NoContent();
        }

    }
}