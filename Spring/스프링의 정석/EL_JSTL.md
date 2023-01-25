# EL(Expression Language)
<% 값 %>  -> ${값}

```html
<body>
    person.getCar().getColor() = <%= persion.getCar().getColor() %> <br>
    person.getCar().getColor() = ${ person.getCar().getColor() } <br>
    person.getCar().getColor() = ${ person.car.color } <br>
</body>

```
```html
<body>
    name = <%= request.getAttribute("name") %> <br>
    name = ${ requestScope.name } <br>
    name = ${ name }
</body>
```
```html
<body>
    id = <%= request.getParameter("id") %>
    id = ${pageContext.request.getParameter("id")} <br>
    id = ${param.id} <br>
</body>
```
```html
<body>
    "1"+1 = ${"1"+1}<br>
</body>
```
