using Microsoft.EntityFrameworkCore;
using APIService.Service.Interface.Settings;
using APIService.Entity;
using APIService.DataAccess;

namespace APIService.Service.Service.Settings
{
    public class SettingService : ISettingService
    {
        private readonly TriolingoDbContext _context;
        public SettingService(TriolingoDbContext context)
        {
            _context = context;
        }

        public List<Setting> GetAllSetting()
        {
            var settings = _context.Settings.ToList();
            //var result = _mapper.Map<List<SettingDto>>(settings);
            return settings;
        }

        public List<Setting> GetSettingByParentId(int? settingId)
        {
            var settings = _context.Settings.Where(x => x.ParentId == settingId).ToList();
            //var result = _mapper.Map<List<SettingDto>>(settings);
            return settings;
        }

        public async Task<bool> DeactiveSetting(int? settingId)
        {
            var settingg = await _context.Settings.Where(x => x.Id == settingId).FirstOrDefaultAsync();
            if (settingg != null)
            {
                settingg.Status = 0;
                await _context.SaveChangesAsync();
                return true;
            }
            return false;
        }
        public async Task<bool> AddNewSetting(Setting setting)
        {
            Setting set = new Setting()
            {
                Name = setting.Name,
                Status = 1,
                Note = setting.Note,
                Value = setting.Value,
                ParentId = setting.ParentId,
            };
            await _context.Settings.AddAsync(set);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> EditSetting(Setting setting)
        {
            var oldEntity = await _context.Settings.Where(x => x.Id == setting.Id).FirstOrDefaultAsync();
            if (oldEntity != null)
            {
                oldEntity.Name = setting.Name;
                oldEntity.Note = setting.Note;
                oldEntity.Value = setting.Value;
                oldEntity.ParentId = setting.ParentId;
                _context.Update(oldEntity);
                await _context.SaveChangesAsync();
                return true;
            }
            return false;
        }
        public async Task<bool> ActiveSetting(int? settingId)
        {
            var settingg = await _context.Settings.Where(x => x.Id == settingId).FirstOrDefaultAsync();
            if (settingg != null)
            {
                settingg.Status = 1;
                await _context.SaveChangesAsync();
                return true;
            }
            return false;
        }
        public async Task<Setting> GetSettingById(int? id)
        {
            var settings = await _context.Settings.Where(x => x.Id == id).FirstOrDefaultAsync();
            //var result = _mapper.Map<SettingDto>(settings);
            return settings;
        }

        public List<Setting> OrderSettingsParent()
        {
            List<Setting> temp = new List<Setting>();
            temp = GetSettingsNoParentId();
            List<Setting> result = new List<Setting>();
            foreach (var item in temp)
            {
                result.Add(item);
                result.AddRange(GetSettingByParentId(item.Id));
            }
            return result;
        }

        public List<Setting> GetSettingsNoParentId()
        {
            var settings = _context.Settings.Where(x => x.ParentId == null).ToList();
            //var result = _mapper.Map<List<SettingDto>>(settings);
            return settings;
        }

        public bool IsDuplicateSetting(Setting item)
        {
            var val = _context.Settings.Where(x => x.Name == item.Name
            || x.Value == item.Value).FirstOrDefault();
            if (val == null)
            {
                return false;
            }
            return true;
        }

    }
}
