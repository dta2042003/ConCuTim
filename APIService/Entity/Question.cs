using System;
using System.Collections.Generic;

namespace APIService.Entity
{
    public class Question
    {
        public int Id { get; set; }
        public string Question1 { get; set; }
        public int Status { get; set; }
        public Exercise Exercise { get; set; }
        public int ExerciseId { get; set; }
        public int Mark { get; set; }
    }
}
