using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace Lab2.Migrations
{
    /// <inheritdoc />
    public partial class initial : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_Creator",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Firstname = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Lastname = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Login = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Password = table.Column<string>(type: "text", maxLength: 128, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_Creator", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_Label",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "text", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_Label", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_Issue",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Title = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Content = table.Column<string>(type: "text", maxLength: 2048, nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    CreatorId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_Issue", x => x.Id);
                    table.ForeignKey(
                        name: "FK_tbl_Issue_tbl_Creator_CreatorId",
                        column: x => x.CreatorId,
                        principalTable: "tbl_Creator",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_Issue_Labels",
                columns: table => new
                {
                    IssueId = table.Column<long>(type: "bigint", nullable: false),
                    LabelId = table.Column<long>(type: "bigint", nullable: false),
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_Issue_Labels", x => new { x.IssueId, x.LabelId });
                    table.ForeignKey(
                        name: "FK_tbl_Issue_Labels_tbl_Issue_IssueId",
                        column: x => x.IssueId,
                        principalTable: "tbl_Issue",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_tbl_Issue_Labels_tbl_Label_LabelId",
                        column: x => x.LabelId,
                        principalTable: "tbl_Label",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_Note",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Content = table.Column<string>(type: "text", maxLength: 2048, nullable: false),
                    IssueId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_Note", x => x.Id);
                    table.ForeignKey(
                        name: "FK_tbl_Note_tbl_Issue_IssueId",
                        column: x => x.IssueId,
                        principalTable: "tbl_Issue",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_Issue_CreatorId",
                table: "tbl_Issue",
                column: "CreatorId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_Issue_Labels_LabelId",
                table: "tbl_Issue_Labels",
                column: "LabelId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_Note_IssueId",
                table: "tbl_Note",
                column: "IssueId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_Issue_Labels");

            migrationBuilder.DropTable(
                name: "tbl_Note");

            migrationBuilder.DropTable(
                name: "tbl_Label");

            migrationBuilder.DropTable(
                name: "tbl_Issue");

            migrationBuilder.DropTable(
                name: "tbl_Creator");
        }
    }
}
