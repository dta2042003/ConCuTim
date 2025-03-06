using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using APIService.Entity;

namespace APIService.Configuration
{
    public class StudentLessonConfiguration : IEntityTypeConfiguration<StudentLesson>
    {
        public void Configure(EntityTypeBuilder<StudentLesson> builder)
        {
            builder.ToTable("StudentLesson");
            builder.HasKey(x => x.Id);
            builder.Property(x => x.Mark).IsRequired();
            builder.Property(x => x.Note);
            builder.HasOne(x => x.StudentCourse).WithMany().HasForeignKey(x => x.StudentCourseId).OnDelete(DeleteBehavior.NoAction);
        }
    }
}
