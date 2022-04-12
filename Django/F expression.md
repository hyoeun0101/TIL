# 동시성 문제 해결하기
동시에 좋아요 누르면?   
기존 코드
```

def do_like(article_id: int, user_id: int) -> Like:
  User.objects.get(id=user_id)
  article = Article.objects.get(id=article_id)
  article.like_count += 1
  article.save()
  
  return Like.objects.create(user_id=user_id, article_id=article_id)
  ```
  
이렇게 작성하면    
서버에서 요청-> DB에서 like_count 가져오기 -> like_count +1 해주기 -> DB저장   
그래서 동시에 요청이 가면 하나만 증가한다.

## F expression으로 해결!!

```
from django.db.models import F

def do_like(article_id: int, user_id: int) -> Like:
  User.objects.get(id=user_id)
  Article.objectsget(id=article_id).update(like_count=F("like_count")+1)
  
  return Like.objects.create(user_id=user_id, article_id=article_id)
```
바로 DB에 요청!!   
SQL구문.
