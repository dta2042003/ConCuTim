using APIService.DataAccess;
using APIService.Service.Service.Courses;
using APIService.Service.Service.Exercises;
using APIService.Service.Interface.Courses;
using APIService.Service.Interface.Exercises;
using APIService.Service.Interface.Lessons;
using APIService.Service.Interface.QnA;
using APIService.Service.Interface.Settings;
using APIService.Service.Interface.Units;
using APIService.Service.Interface.Users;
using APIService.Service.Service.Lessons;
using APIService.Service.Service.QnA;
using APIService.Service.Service.Settings;
using APIService.Service.Service.Units;
using APIService.Service.Service.Users;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
var configuration = builder.Configuration;
string connectionString = configuration.GetConnectionString("TriolingoConStr");
builder.Services.AddDbContext<TriolingoDbContext>(options =>
        options.UseSqlServer(connectionString));
#region regiter DI service
builder.Services.AddTransient<IExercise, ExerciseService>();
builder.Services.AddTransient<IAnswer, AnswerService>();
builder.Services.AddTransient<IQuestion, QuestionService>();
builder.Services.AddTransient<ICourseService, CourseService>();
builder.Services.AddTransient<ISettingService, SettingService>();
builder.Services.AddTransient<ILessonService, LessonService>();
builder.Services.AddTransient<IUserService, UserService>();
builder.Services.AddTransient<IUnitService, UnitService>();
#endregion
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment()||app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
