## Field 심화

1. \_\_startswith : 시작하는 문자열

```
Question.objects.filter(subject__startswith='What')
```

2. \_\_endswith : 끝나는 문자열

```
Question.objects.filter(subject__endswith='this')
```

3. DateField 에서 쓸 수 있음

- \_\_gte : 크거나 같다
- \_\_gt : 크다
- \_\_Ite : 작거나 같다
- \_\_It : 작다
- \_\_range : 범위

```
create_at = models.DateField()

Question.objects.filter(create_at__gte=datetime.today())
Question.objects.filter(create_at__range=(datetime.date(2021,2,20),datetime.date(2021,4,20)))

```
