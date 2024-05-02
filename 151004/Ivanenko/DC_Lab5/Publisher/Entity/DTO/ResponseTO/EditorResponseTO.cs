using Newtonsoft.Json;

namespace Publisher.Entity.DTO.ResponseTO
{
    public record class EditorResponseTO(int Id,string Login, string Firstname,string Lastname);
}
