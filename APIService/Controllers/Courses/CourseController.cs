using APIService.Entity;
using APIService.Service.Interface;
using APIService.Service.Interface.Courses;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace APIService.Controllers.Courses
{
    [Route("api/[controller]")]
    [ApiController]
    public class CourseController : ControllerBase
    {
        private readonly ICourseService _courseService;
        public CourseController(ICourseService courseService)
        {
            _courseService = courseService;
        }
        [HttpGet("get")]
        public async Task<IActionResult> GetAllCourse(string? filter = "")
        {
            try
            {
                var list = await _courseService.GetAllCourse();
                return Ok(list);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
        [HttpGet("detail/{id}")]

        public async Task<IActionResult> GetCourse(int id)
        {
            try
            {
                var course = await _courseService.GetCourseById(id);
                return Ok(course);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }

        [HttpPost("add")]
        public async Task<IActionResult> AddCourse([FromBody] Course co)
        {
            try
            {
                if (await _courseService.AddNewCourse(co)>0)
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
        public async Task<IActionResult> UpdateCourse([FromBody] Course co)
        {
            try
            {
                if (await _courseService.EditCourse(co))
                    return NoContent();
                else return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
        //[HttpDelete("id")]
        //public async Task<IActionResult> DeleteCourse(int coId)
        //{
        //    try
        //    {
        //        if (await _courseService.DeleteCourse(coId)) return Ok();
        //        else return BadRequest();
        //    }
        //    catch(Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //        throw;
        //    }
        //}
    }
}
