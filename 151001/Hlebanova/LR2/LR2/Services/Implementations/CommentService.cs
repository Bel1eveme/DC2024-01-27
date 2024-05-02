using Microsoft.EntityFrameworkCore;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Exceptions;
using LR2.Services.Interfaces;
using LR2.Storage;
using CommentMapper = LR2.Mappers.CommentMapper;

namespace LR2.Services.Implementations;

public sealed class CommentService(AppDbContext context) : ICommentService
{
    public async Task<CommentResponseTo> GetCommentById(long id)
    {
        var Comment = await context.Comments.FindAsync(id);
        if (Comment == null) throw new EntityNotFoundException($"Comment with id = {id} doesn't exist.");

        return CommentMapper.Map(Comment);
    }

    public async Task<IEnumerable<CommentResponseTo>> GetComments()
    {
        return CommentMapper.Map(await context.Comments.ToListAsync());
    }

    public async Task<CommentResponseTo> CreateComment(CreateCommentRequestTo createCommentRequestTo)
    {
        var Comment = CommentMapper.Map(createCommentRequestTo);
        await context.Comments.AddAsync(Comment);
        await context.SaveChangesAsync();
        return CommentMapper.Map(Comment);
    }

    public async Task DeleteComment(long id)
    {
        var Comment = await context.Comments.FindAsync(id);
        if (Comment == null) throw new EntityNotFoundException($"Comment with id = {id} doesn't exist.");

        context.Comments.Remove(Comment);
        await context.SaveChangesAsync();
    }

    public async Task<CommentResponseTo> UpdateComment(UpdateCommentRequestTo modifiedComment)
    {
        var Comment = await context.Comments.FindAsync(modifiedComment.Id);
        if (Comment == null) throw new EntityNotFoundException($"Comment with id = {modifiedComment.Id} doesn't exist.");

        context.Entry(Comment).State = EntityState.Modified;

        Comment.Id = modifiedComment.Id;
        Comment.Content = modifiedComment.Content;
        Comment.IssueId = modifiedComment.IssueId;

        await context.SaveChangesAsync();
        return CommentMapper.Map(Comment);
    }
}