using Microsoft.EntityFrameworkCore;
using APIService.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace APIService.Configuration
{
    public class CourseConfiguration : IEntityTypeConfiguration<Course>
    {
        public void Configure(EntityTypeBuilder<Course> builder)
        {
            builder.ToTable("Course");
            builder.HasKey(x => x.Id);
            builder.Property(x => x.Name).HasMaxLength(150).IsRequired();
            builder.Property(x => x.RateAverage);
            builder.Property(x => x.Status).HasDefaultValue(1).IsRequired();
            builder.Property(x => x.Note);
            builder.Property(x => x.Description).IsRequired();
        }
    }
}
