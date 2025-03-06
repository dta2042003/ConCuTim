using APIService.Entity;
using APIService.Service.Interface.Lessons;
using APIService.Service.Interface.Units;
using APIService.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Text.RegularExpressions;

namespace APIService.Controllers.Lessons
{
    [Route("api/[controller]")]
    [ApiController]
    public class LessonController : ControllerBase
    {
        private readonly ILessonService _lessonService;
        private readonly IUnitService _unitService;
        public LessonController(ILessonService lessonService, IUnitService unitService)
        {
            _lessonService = lessonService;
            _unitService = unitService;
        }
        [HttpPost("get")]
        public async Task<IActionResult> GetLessonsByUnitId(string? filter = "")
        {
            try
            {
                List<Lesson> list = new List<Lesson>();
                if (!String.IsNullOrEmpty(filter))
                {
                    var match = Regex.Match(filter, @"id==(\d+)");
                    if (match.Success)
                    {
                        var id = int.Parse(match.Groups[1].Value);
                        var unit = await _unitService.GetById(id);
                        if (unit == null)
                        {
                            return NotFound();
                        }
                        list = await _lessonService.getAllLessonsByUnitId(id);
                    }
                }
                else
                {
                    list = await _lessonService.GetAllLesson();
                }
                if (list.Count == 0)
                {
                    return Ok("Nothing in list");
                }
                List<LessonModel> result = new List<LessonModel>();
                foreach (var item in list)
                {
                    result.Add(new LessonModel()
                    {
                        Id = item.Id,
                        Name = item.Name,
                        Description = item.Description,
                        Note = item.Note,
                        Status = item.Status
                    });
                }
                return Ok(result);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
        [HttpPost("add")]
        public async Task<IActionResult> AddUnit([FromBody] LessonModel lesson)
        {
            try
            {
                Lesson le = new Lesson()
                {
                    Name = lesson.Name,
                    UnitId = lesson.UnitId,
                    Description = lesson.Description,
                    Note = lesson.Note,
                    Status = 1
                };
                if (await _lessonService.AddLesson(le))
                {
                    return Ok(le);
                }
                else { return BadRequest(); }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
                throw;
            }
        }
    }
}
