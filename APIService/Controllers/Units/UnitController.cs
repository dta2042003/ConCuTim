using APIService.Entity;
using APIService.Service.Interface.Courses;
using APIService.Service.Interface.Units;
using APIService.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Text.RegularExpressions;
using static Microsoft.EntityFrameworkCore.DbLoggerCategory;

namespace APIService.Controllers.Units
{
    [Route("api/[controller]")]
    [ApiController]
    public class UnitController : ControllerBase
    {
        private readonly IUnitService _unitService;
        private readonly ICourseService _courseService;
        public UnitController(IUnitService unitService, ICourseService courseService)
        {
            _unitService = unitService;
            _courseService = courseService;
        }
        [HttpPost("get")]
        public async Task<IActionResult> GetUnitsByCourseId(string? filter = "")
        {
            try
            {
                List<Unit> list = new List<Unit>();
                if (!String.IsNullOrEmpty(filter))
                {
                    var match = Regex.Match(filter, @"id==(\d+)");
                    if (match.Success)
                    {
                        var id = int.Parse(match.Groups[1].Value);
                        var course = await _courseService.GetCourseById(id);
                        if (course == null)
                        {
                            return NotFound();
                        }
                        list = await _unitService.GetUnitsByCourseId(id);
                    }
                }
                else
                {
                    list = await _unitService.GetAll();
                }
                if (list.Count == 0)
                {
                    return Ok("Nothing in list");
                }
                List<UnitModel> result = new List<UnitModel>();
                foreach (var unit in list)
                {
                    result.Add(new UnitModel()
                    {
                        Id = unit.Id,
                        Name = unit.Name,
                        Description = unit.Description,
                        Note = unit.Note,
                        Order = unit.Order,
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
        public async Task<IActionResult> AddUnit([FromBody] UnitModel unit)
        {
            try
            {
                Unit u = new Unit()
                {
                    Name = unit.Name,
                    Description = unit.Description,
                    CourseId = unit.CourseId,
                    Note = unit.Note,
                };
                if (await _unitService.AddUnit(u))
                {
                    return Ok(u);
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
