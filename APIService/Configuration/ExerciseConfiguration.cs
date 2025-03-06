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
    public class ExerciseConfiguration : IEntityTypeConfiguration<Exercise>
    {
        public void Configure(EntityTypeBuilder<Exercise> builder)
        {
            builder.ToTable("Exercise");
            builder.HasKey(e => e.Id);
            builder.Property(e => e.Status).HasDefaultValue(1).IsRequired();
            builder.Property(e => e.Title).HasMaxLength(250).IsRequired();
            builder.Property(e => e.Description);
            builder.Property(e => e.File);
            builder.Property(e => e.FileName);
            builder.HasOne(x => x.Setting).WithMany().HasForeignKey(x => x.TypeId).OnDelete(DeleteBehavior.NoAction);
        }
    }
}
