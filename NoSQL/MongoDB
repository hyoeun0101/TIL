```
from pymongo import MongoClient
client = MongoClient('...')
db = client.dbuser

# 생성
db.users.insert_one(doc)

# 모두 찾기
all_users = list(db.users.find({},{'_id':False}))

#하나 찾기
db.users.find_one({'name':'eun'},{'_id':False})

#수정
db.users.update_one({'name':'eun'}, {'$set': {'age':23}})

#삭제
db.users.delete_one({'name':'eun'})
```
