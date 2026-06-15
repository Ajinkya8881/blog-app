Complete Test List
TAGS
1.  GET    /api/tags                         → No Auth,  No Body
2.  POST   /api/tags                         → Admin,    {"name": "Java"}
3.  POST   /api/tags                         → Admin,    {"name": "Spring"}
4.  PUT    /api/tags/1                       → Admin,    {"name": "Java Programming"}
5.  DELETE /api/tags/1                       → Admin,    No Body
6.  POST   /api/tags                         → User,     {"name": "Test"} → expect 403
7.  DELETE /api/tags/2                       → No Auth,  No Body → expect 401
    POSTS
8.  GET    /api/posts                        → No Auth,  No Body
9.  GET    /api/posts?page=0&size=5          → No Auth,  No Body
10. GET    /api/posts?search=java            → No Auth,  No Body
11. GET    /api/posts?sort=latest            → No Auth,  No Body
12. GET    /api/posts?tagId=2               → No Auth,  No Body
13. GET    /api/posts?author=ajinkya        → No Auth,  No Body
14. POST   /api/posts                        → User(ajinkya), Body:
    {
    "title": "My Java Post",
    "excerpt": "About Java",
    "content": "Java is awesome",
    "tagIds": [2]
    }
15. POST   /api/posts                        → Admin, Body:
    {
    "title": "Admin Post",
    "excerpt": "By admin",
    "content": "Admin content",
    "author": "customAuthor",
    "tagIds": [2]
    }
16. GET    /api/posts/1                      → No Auth,  No Body
17. PUT    /api/posts/1                      → User(ajinkya - owner), Body:
    {
    "title": "Updated Java Post",
    "excerpt": "Updated excerpt",
    "content": "Updated content",
    "tagIds": [2]
    }
18. PUT    /api/posts/1                      → Admin, Body:
    {
    "title": "Admin Edited Post",
    "excerpt": "Admin excerpt",
    "content": "Admin edited content",
    "author": "someoneElse",
    "tagIds": [2]
    }
19. PUT    /api/posts/1                      → User(other user) → expect 403
20. DELETE /api/posts/1                      → User(other user) → expect 403
21. DELETE /api/posts/1                      → User(ajinkya - owner), No Body
    COMMENTS
22. POST   /api/posts/2/comments             → No Auth, Body:
    {
    "name": "John",
    "email": "john@example.com",
    "comment": "Great post!"
    }
23. POST   /api/posts/2/comments             → No Auth, Body:
    {
    "name": "Jane",
    "email": "jane@example.com",
    "comment": "Very helpful!"
    }
24. PUT    /api/posts/2/comments/1           → User(ajinkya - post owner), Body:
    {
    "comment": "Updated comment text"
    }
25. PUT    /api/posts/2/comments/1           → Admin, Body:
    {
    "comment": "Admin edited comment"
    }
26. PUT    /api/posts/2/comments/1           → User(other user) → expect 403
27. DELETE /api/posts/2/comments/1           → User(ajinkya - post owner), No Body
28. DELETE /api/posts/2/comments/2           → Admin, No Body
29. DELETE /api/posts/2/comments/2           → No Auth → expect 401




    blog-app-production-a92f.up.railway.app