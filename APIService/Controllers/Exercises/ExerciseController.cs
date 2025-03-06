using APIService.Entity;
using APIService.Service.Interface.Exercises;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Data;

namespace APIService.Controllers.Exercises
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExerciseController : Controller
    {
        private readonly IExercise _courseService;
        public ExerciseController(IExercise courseService)
        {
            _courseService = courseService;
        }
        [HttpGet("get")]
        public IActionResult GetAllExercise(string? filter = "")
        {
            try
            {
                return Ok(_courseService.GetAllExercise());
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }

        [HttpGet("detail/{id}")]
        public async Task<IActionResult> GetExercise(int id)
        {
            try
            {
                var ex = await _courseService.GetExerciseById(id);
                return Ok(ex);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }

        [HttpPost("add")]
        public async Task<IActionResult> AddExercise([FromBody] Exercise co)
        {
            try
            {
                if (await _courseService.AddExercise(co) > 0)
                    return Ok(co);
                else return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
        [HttpPut("update")]
        public async Task<IActionResult> UpdateExercise([FromBody] Exercise co)
        {
            try
            {
                if (await _courseService.UpdateExercise(co))
                    return NoContent();
                else return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
        [HttpDelete("delete/{coId}")]
        public async Task<IActionResult> DeleteCourse(int coId)
        {
            try
            {
                if (await _courseService.DeleteExercise(coId)) return Ok();
                else return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
    }
}
