using System;
using System.Collections.Generic;

namespace APIService.Entity
{
    public class Answer
    {
        public int Id { get; set; }
        public Question Question { get; set; }
        public int QuestionId { get; set; }
        public string? Answer1 { get; set; }
        public int Status { get; set; }
        public bool IsCorrect { get; set; }
    }
}
