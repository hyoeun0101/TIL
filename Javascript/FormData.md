### { key : value } => Json 구조

__FormData.get, Form.getall()__
```js
const form_data = new FormData()
form_data.append("name", "eun")
form_data.append("name", "Tom")

form_data.get("name")
>>> 'eun'
let a = form_data.get('name')
console.log(a)
>>> eun

form_data.getAll('name')
>>>['eun','Tom']
```

