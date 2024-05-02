using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab2.Migrations
{
    /// <inheritdoc />
    public partial class unique : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateIndex(
                name: "IX_tbl_Label_Name",
                table: "tbl_Label",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_Issue_Title",
                table: "tbl_Issue",
                column: "Title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_Creator_Login",
                table: "tbl_Creator",
                column: "Login",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_tbl_Label_Name",
                table: "tbl_Label");

            migrationBuilder.DropIndex(
                name: "IX_tbl_Issue_Title",
                table: "tbl_Issue");

            migrationBuilder.DropIndex(
                name: "IX_tbl_Creator_Login",
                table: "tbl_Creator");
        }
    }
}
