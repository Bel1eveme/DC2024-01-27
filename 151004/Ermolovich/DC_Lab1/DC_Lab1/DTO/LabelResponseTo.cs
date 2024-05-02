using DC_Lab1.DTO.Interface;

namespace DC_Lab1.DTO
{
    public class LabelResponseTo(int Id, string? Name) : IResponseTo
    {
        public int Id { get; set; } = Id;

        public string? name { get; set; } = Name;
    }
}
