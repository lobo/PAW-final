---
title: API Reference

language_tabs: # must be one of https://git.io/vQNgJ
  - shell
  - javascript

toc_footers:
  - <a href='https://github.com/lord/slate'>Documentation Powered by Slate</a>

includes:
  - errors

search: true
---

# Introduction

Welcome to the ClickerQuest API! Here you can see how to access all of the ClickerQuest API in order to be able to play the game.

We have language bindings in Javascript and Shell! You can view code examples in the dark area to the right, and you can switch the programming language of the examples with the tabs in the top right.

This API documentation page was created with [Slate](https://github.com/lord/slate).

# Authentication

> To authorize, use this code:

```shell
# With shell, you can just pass the correct header with each request
curl "api_endpoint_here"
  -H "Authorization: Bearer <token>"

```

```javascript
$http({ method: 'POST',
        url: "api_endpoint_here",
        headers: {'Authorization': "Bearer <token>",
        data: body
    }).then(...)
```

> Make sure to replace `<token>` with your token.

ClickerQuest uses a Token Authorization method. In order to get be recognized add the following header.
`Authorization: Bearer <token>`
To get your token, you must login first at the specified Endpoint

<aside class="notice">
You must replace <code>&lt token &gt</code> with your personal token.
</aside>

# Accounts

## Create User

```shell
curl -X POST http://localhost:8080/api/v1/accounts/create 
     -d '{"username":"MyUser", "password":"12345678"}' 
     -H "Content-Type: application/json" 
```

```javascript
$http.post("http://localhost:8080/api/v1/accounts/create", { username:"MyUser", password: "12345678"}).then(...);
 ```
> The above command returns no JSON but a 201 status code created with a header:

```json
  Location: http://localhost:8080/api/v1/accounts/create
```

### HTTP Request

`POST http://localhost:8080/api/v1/users/all`

### Query Parameters

Parameter | Description
--------- | -----------
username | Username for the new user
password | Password to authenticate the new user

### Errors
Code | Description
--------- | -----------
419 | Username already exists.

## Login User

```shell
curl -X POST http://localhost:8080/api/v1/accounts/login 
     -d 'username=MyUser&password=12345678' 
     -H "Content-Type: application/x-www-form-urlencoded" 
```

```javascript
$http({
    method: 'POST',
    url: "http://localhost:8080/api/v1/accounts/login", 
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    data: "username=MyUser&password=12345678"
    }).then(...)
 ```
> The above command returns no JSON but a 200 status code created with a header:

```json
  Authorization: Bearer <token>
```

This endpoint provides you with a token given correct credentials.
You must save this token for any future request that require it. You can always generate a new token by login in again.

<aside class="notice">
Make sure to send the request parameters in a <code>application/x-www-form-urlencoded</code> format
</aside>

### HTTP Request

`POST http://localhost:8080/api/v1/accounts/login`

### Query Parameters

Parameter | Description
--------- | -----------
username | Username for the user
password | Password to authenticate the user

## Get current User

```shell
curl -X GET http://localhost:8080/api/v1/accounts/user 
     -H "Authorization: Bearer <token>"
```

```javascript
$http({
    method: 'GET',
    url: "http://localhost:8080/api/v1/accounts/user", 
    headers: {'Authorization': 'Bearer <token>'}
    }).then(...)
 ```
> The above command returns JSON structured like this:

```json
{
    "id": 44,
    "username": "MyUser",
    "profile_image_url": "http://localhost:8080/api/resources/profile_images/2.jpg",
    "score": 12.7,
    "factories_url": "http://localhost:8080/api/v1/users/44/factories",
    "wealth_url": "http://localhost:8080/api/v1/users/44/wealth",
    "rank_url": "http://localhost:8080/api/v1/users/44/rank"
}
```

This endpoint provides you with the user corresponding at the a token given.

### HTTP Request

`GET http://localhost:8080/api/v1/accounts/user`

# Users

## Get All Users

```shell
curl -X GET -G "http://localhost:8080/api/v1/users/all" -d page=3 -d pageSize=2
```

```javascript
$http.get("http://localhost:8080/api/v1/users/all", { params: { page: 3, pageSize: 2 } }).then(...);
 ```

> The above command returns JSON structured like this:

```json
{
    "page": 3,
    "items_per_page": 2,
    "total_pages": 4,
    "total_items": 8,
    "elements": [
        {
            "type": "User",
            "id": 41,
            "username": "asdasd",
            "profile_image_url": "http://localhost:8080/api/resources/profile_images/1.jpg",
            "score": 72.09,
            "clan_id": 20,
            "clan_url": "http://localhost:8080/api/v1/clans/20",
            "factories_url": "http://localhost:8080/api/v1/users/41/factories",
            "wealth_url": "http://localhost:8080/api/v1/users/41/wealth",
            "rank_url": "http://localhost:8080/api/v1/users/41/rank"
        },
        {
            "type": "User",
            "id": 40,
            "username": "kokoko",
            "profile_image_url": "http://localhost:8080/api/resources/profile_images/5.jpg",
            "score": 12.7,
            "clan_id": 14,
            "clan_url": "http://localhost:8080/api/v1/clans/14",
            "factories_url": "http://localhost:8080/api/v1/users/40/factories",
            "wealth_url": "http://localhost:8080/api/v1/users/40/wealth",
            "rank_url": "http://localhost:8080/api/v1/users/40/rank"
        }
    ],
    "next": "http://localhost:8080/api/v1/users/all/?page=4&pageSize=2",
    "previous": "http://localhost:8080/api/v1/users/all/?page=2&pageSize=2"
}
```

This endpoint retrieves all the users ordered by score paginated by the given parameters.

<aside class="notice">
If page parameter is outside of its limits, a 404 status code will be returned.
</aside>

### HTTP Request

`GET http://localhost:8080/api/v1/users/all`

### Query Parameters

Parameter | Default | Description
--------- | ------- | -----------
page | 1 | Number of page of user to be provided.
pageSize | 20 | Number of users per page.

## Get a specific User

```shell
curl "http://localhost:8080/api/v1/users/1"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "id": 1,
    "username": "Wololo",
    "profile_image_url": "http://localhost:8080/api/resources/profile_images/10.jpg",
    "score": 122504379.20514345,
    "clan_id": 20,
    "clan_url": "http://localhost:8080/api/v1/clans/20",
    "factories_url": "http://localhost:8080/api/v1/users/1/factories",
    "wealth_url": "http://localhost:8080/api/v1/users/1/wealth",
    "rank_url": "http://localhost:8080/api/v1/users/1/rank"
}
```

This endpoint retrieves a specific user.

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the user to retrieve

## Get an User's Wealth

```shell
curl "http://localhost:8080/api/v1/users/1/wealth"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/wealth").then(...);
```
> The above command returns JSON structured like this:

```json
{
    "user_id": 1,
    "resources": [
        {
            "id": 12,
            "name": "CIRCUITS",
            "storage": 21134738,
            "production": 8
        },
        {
            "id": 11,
            "name": "CARDBOARD",
            "storage": 263214871813,
            "production": 780692.0363048307
        },
        {
            "id": 10,
            "name": "COPPER_CABLE",
            "storage": 8374086,
            "production": 168
        },
        {
            "id": 9,
            "name": "COPPER",
            "storage": 516261829,
            "production": 259.88
        },
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "storage": 79085012530,
            "production": 232536.14034896545
        },
        {
            "id": 7,
            "name": "RUBBER",
            "storage": 464732104,
            "production": 4202.6
        },
        {
            "id": 6,
            "name": "TIRES",
            "storage": 25782182645,
            "production": 65052.24
        },
        {
            "id": 5,
            "name": "IRON",
            "storage": 16971794,
            "production": 1447.32
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "storage": 4203671603,
            "production": 358531.425
        },
        {
            "id": 3,
            "name": "MONEY",
            "storage": 653106305951,
            "production": 11538657
        },
        {
            "id": 2,
            "name": "GOLD",
            "storage": 0,
            "production": 0
        },
        {
            "id": 1,
            "name": "PLASTIC",
            "storage": 123,
            "production": 0
        },
        {
            "id": 0,
            "name": "POWER",
            "storage": 1096833872,
            "production": 80.90000000000002
        }
    ]
}
```

This endpoint returns a given user's wealth.

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/wealth`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the wealth's user to retrieve

## Get an User's factories

```shell
curl "http://localhost:8080/api/v1/users/1/factories"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/factories").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "user_id": 1,
    "factories": [
        {
            "id": 3,
            "type": "METAL_SEPARATOR",
            "amount": 1723,
            "level": 24,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/3/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/3/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/3/recipe"
        },
        {
            "id": 4,
            "type": "RUBBER_SHREDDER",
            "amount": 6722,
            "level": 0,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/4/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/4/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/4/recipe"
        },
        {
            "id": 2,
            "type": "JUNK_COLLECTOR",
            "amount": 341288,
            "level": 5,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/2/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/2/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/2/recipe"
        },
        {
            "id": 7,
            "type": "CIRCUIT_MAKER",
            "amount": 4,
            "level": 0,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/7/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/7/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/7/recipe"
        },
        {
            "id": 6,
            "type": "BOILER",
            "amount": 458,
            "level": 2,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/6/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/6/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/6/recipe"
        },
        {
            "id": 0,
            "type": "STOCK_INVESTOR",
            "amount": 5917260,
            "level": 39,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/0/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/0/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/0/recipe"
        },
        {
            "id": 1,
            "type": "PEOPLE_RECRUITER",
            "amount": 1138195,
            "level": 2,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/1/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/1/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/1/recipe"
        },
        {
            "id": 5,
            "type": "CABLE_MAKER",
            "amount": 235,
            "level": 0,
            "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/5/upgrade",
            "buyLimits_url": "http://localhost:8080/users/1/factories/5/buyLimits",
            "recipe_url": "http://localhost:8080/api/v1/users/1/factories/5/recipe"
        }
    ]
}
```

This endpoint returns a given user's factories.

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/factories`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the factories' user to retrieve

## Get an User's specific factory

```shell
curl "http://localhost:8080/api/v1/users/1/factories/0"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/factories/0").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "id": 1,
    "type": "PEOPLE_RECRUITER",
    "amount": 1138195,
    "level": 2,
    "upgradeURL": "http://localhost:8080/api/v1/users/1/factories/1/upgrade",
    "buyLimits_url": "http://localhost:8080/users/1/factories/1/buyLimits",
    "recipe_url": "http://localhost:8080/api/v1/users/1/factories/1/recipe"
}
```

This endpoint returns a given user's specific factory.

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/factories/<FactoryID>`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the factory's user to retrieve
FactoryID | The ID of the factoryType to retrieve

## Get an User's factory's recipe

```shell
curl "http://localhost:8080/api/v1/users/1/factories/1/recipe"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/factories/1/recipe").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "user_id": 1,
    "factory_id": 1,
    "recipe": [
        {
            "id": 4,
            "name": "PEOPLE",
            "production": 0.315
        },
        {
            "id": 3,
            "name": "MONEY",
            "storage": 212249383
        }
    ]
}
```

This endpoint returns a given user's specific factory's recipe.

<aside class="notice">
Storage represents the cost in resources for that factory and productions the amount of production it needs to consume of that given resource
</aside>

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/factories/<FactoryID>/recipe`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the factory's user to retrieve
FactoryID | The ID of the factoryType to retrieve


## Get an User's factory's upgrade

```shell
curl "http://localhost:8080/api/v1/users/1/factories/1/upgrade"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/factories/1/upgrade").then(...);
```


> The above command returns JSON structured like this:

```json
{
    "type": 2,
    "factory_type_id": 1,
    "factory_type": "PEOPLE_RECRUITER",
    "level": 3,
    "description": "Upgrade n°3",
    "cost": 900
}
```

This endpoint returns a given user's specific factory's upgrade.

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/factories/<FactoryID>/upgrade`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the factory's user to retrieve
FactoryID | The ID of the factoryType to retrieve

## Get an User's factory's Buy Limits


```shell
curl "http://localhost:8080/api/v1/users/1/factories/1/buyLimits"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/factories/1/buyLimits").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "user_id": 1,
    "factory_id": 3,
    "max": 85663,
    "cost_max": [
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "production": 256989
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "storage": 4360284364
        }
    ],
    "cost_next_max": [
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "production": 256992
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "storage": 4360384197
        }
    ],
    "cost_1": [
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "production": 3
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "storage": 1970
        }
    ],
    "cost_10": [
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "production": 30
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "storage": 19747
        }
    ],
    "cost_100": [
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "production": 300
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "storage": 202608
        }
    ]
}
```

This endpoint retrieves the maximum amount of factories of type <factoryID> which the user can buy with the current resources, and the cost of buying 1, 10, 100 and max of the given factory.
It also provides how much resources and production are needed for purchasing max+1 amount, in which case the buyLimits should be recalculated
### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/factories/<factoryID>/buyLimits`

### Path Parameters

Parameter | Description
--------- | -----------
ID | The ID of the factory's user to retrieve
FactoryID | The ID of the factoryType to retrieve


## Get an User's rank

```shell
curl "http://localhost:8080/api/v1/users/1/rank"
```

```javascript
$http.get("http://localhost:8080/api/v1/users/1/rank").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "user_id": 1,
    "user_url": "http://localhost:8080/api/v1/users/1",
    "rank": 1
}
```

This endpoint returns a specific user's rank.

### HTTP Request

`GET http://localhost:8080/api/v1/users/<ID>/rank`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the rank's user to retrieve


# Clans

## Get All Clans


```shell
curl -X GET -G "http://localhost:8080/api/v1/clans/all" -d page=2 -d pageSize=1
```

```javascript
$http.get("http://localhost:8080/api/v1/clans/all", { params: { page: 2, pageSize: 1 } }).then(...);
 ```

> The above command returns JSON structured like this:

```json
{
    "page": 2,
    "items_per_page": 1,
    "total_pages": 4,
    "total_items": 4,
    "elements": [
        {
            "type": "Clan",
            "id": 13,
            "name": "a",
            "score": 1895.770193289029,
            "users_url": "http://localhost:8080/api/v1/clans/13/users",
            "wins": 2,
            "battles": 4135,
            "image": "http://localhost:8080/api/resources/group_icons/1.jpg",
            "battle_url": "http://localhost:8080/api/v1/clans/13/battle",
            "clan_rank_url": "http://localhost:8080/api/v1/clans/13/rank"
        }
    ],
    "next": "http://localhost:8080/api/v1/clans/all/?page=3&pageSize=1",
    "previous": "http://localhost:8080/api/v1/clans/all/?page=1&pageSize=1"
}
```

This endpoint retrieves all clans ordered by score paginated by the given parameters.

### HTTP Request

`GET http://localhost:8080/api/v1/clans/all`

### Query Parameters

Parameter | Default | Description
--------- | ------- | -----------
page | 1 | Number of page of user to be provided.
pageSize | 20 | Number of users per page.

## Get a Specific Clan

```shell
curl "http://localhost:8080/api/v1/clans/13"
```

```javascript
$http.get("http://localhost:8080/api/v1/clans/13").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "id": 13,
    "name": "a",
    "score": 1895.770193289029,
    "users_url": "http://localhost:8080/api/v1/clans/13/users",
    "wins": 2,
    "battles": 4135,
    "image": "http://localhost:8080/api/resources/group_icons/1.jpg",
    "battle_url": "http://localhost:8080/api/v1/clans/13/battle",
    "clan_rank_url": "http://localhost:8080/api/v1/clans/13/rank"
}
```

This endpoint retrieves a specific clan.

### HTTP Request

`GET http://localhost:8080/api/v1/clans/<ID>`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the clan to retrieve

## Get a Clan's users

```shell
curl "http://localhost:8080/api/v1/clans/13/users"
```

```javascript
$http.get("http://localhost:8080/api/v1/clans/13/users").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "clan_id": 13,
    "members": 1,
    "users": [
        {
            "id": 2,
            "username": "aaaa",
            "profile_image_url": "http://localhost:8080/api/resources/profile_images/2.jpg",
            "score": 1895.770193289029,
            "clan_id": 13,
            "clan_url": "http://localhost:8080/api/v1/clans/13",
            "factories_url": "http://localhost:8080/api/v1/users/2/factories",
            "wealth_url": "http://localhost:8080/api/v1/users/2/wealth",
            "rank_url": "http://localhost:8080/api/v1/users/2/rank"
        }
    ]
}
```

This endpoint returns a given clan's users.

### HTTP Request

`GET http://localhost:8080/api/v1/clans/<ID>/users`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the users' clan to retrieve

## Get a Clan's battle

```shell
curl "http://localhost:8080/api/v1/clans/13/battle"
```

```javascript
$http.get("http://localhost:8080/api/v1/clans/13/battle").then(...);
```

> The above command returns JSON structured like this:

```json
{
    "clanId": 13,
    "oppponent_id": 20,
    "clan_url": "http://localhost:8080/api/v1/clans/13",
    "delta_score": 0,
    "opponent_delta_score": 30551941.859066185,
    "opponent_clan_battle_url": "http://localhost:8080/api/v1/clans/20/battle"
}
```

This endpoint returns a given clan's battle.

### HTTP Request

`GET http://localhost:8080/api/v1/clans/<ID>/battle`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the battle's clan to retrieve

## Get a Clan's rank

```shell
curl "http://localhost:8080/api/v1/clans/13/rank"
```

```javascript
$http.get("http://localhost:8080/api/v1/clans/13/rank").then(...);
```


> The above command returns JSON structured like this:

```json
{
    "clan_id": 20,
    "clan_url": "http://localhost:8080/api/v1/clans/20",
    "rank": 1
}
```

This endpoint returns a given clan's rank.

### HTTP Request

`GET http://localhost:8080/api/v1/clans/<ID>/rank`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the rank's clan to retrieve


## Join a Clan

```shell
curl "http://localhost:8080/api/v1/clans/join"
  -X POST
  -d '{"id": 20 }' 
  -H "Authorization: Bearer <token>"
```

```javascript
$http({
    method: 'POST',
    url: "http://localhost:8080/api/v1/clans/join",
    headers: {'Authorization': 'Bearer <token>'},
    data: { id: 20 }
    }).then(...)
```

> The above command returns JSON structured like this:

```json
{
    "id": 20,
    "name": "WololoClan",
    "score": 86035162.84607725,
    "users_url": "http://localhost:8080/api/v1/clans/20/users",
    "wins": 7,
    "battles": 3646,
    "image": "http://localhost:8080/api/v1/resources/group_icons/1.jpg",
    "battle_url": "http://localhost:8080/api/v1/clans/20/battle"
}
```

This endpoint joins the user to a clan.

### HTTP Request

`POST http://localhost:8080/api/v1/clans/join`

### Body Parameters

Parameter | Description
--------- | -----------
id | The ID of the clan to join.

### Errors
Code | Description
--------- | -----------
419 | User is already part of a clan.

## Create a Clan

```shell
curl "http://localhost:8080/api/v1/clans/create"
  -X POST
  -d '{ name: "WololoClan" }' 
  -H "Authorization: Bearer <token>"
```

```javascript
$http({
    method: 'POST',
    url: "http://localhost:8080/api/v1/clans/create",
    headers: {'Authorization': 'Bearer <token>'},
    data: { name: "WololoClan" }
    }).then(...)
```

> The above command returns JSON structured like this:

```json
{
    "id": 20,
    "name": "WololoClan",
    "score": 86035162.84607725,
    "users_url": "http://localhost:8080/api/v1/clans/20/users",
    "wins": 0,
    "battles": 0,
    "image": "http://localhost:8080/api/v1/resources/group_icons/1.jpg",
}
```

This endpoint returns a given clan's battle.

### HTTP Request

`POST http://localhost:8080/api/v1/clans/create`

### Body Parameters

Parameter | Description
--------- | -----------
name | The new clan's name

### Errors
Code | Description
--------- | -----------
419 | User is already part of a clan.
419 | Clan with name <name> already exists.

## Leave a Clan

```shell
curl "http://localhost:8080/api/v1/clans/leave"
  -X DELETE
  -H "Authorization: Bearer <token>"
```

```javascript
$http({
    method: 'DELETE',
    url: "http://localhost:8080/api/v1/clans/leave",
    headers: {'Authorization': 'Bearer <token>'},
    data: { id: 20 }
    }).then(...)
```

> The above command returns no JSON

```json
```

This endpoint removes the user from the clan and deletes the clan if its empty.

### HTTP Request

`DELETE http://localhost:8080/api/v1/clans/leave`

### Errors
Code | Description
--------- | -----------
419 | User is not part of a clan.

# Factories

## Get All factories

```shell
curl "http://example.com/api/v1/users/all"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let kittens = api.kittens.get();
```

> The above command returns JSON structured like this:

```json
{
    "factories": [
        {
            "id": 0,
            "type": "STOCK_INVESTOR",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "PEOPLE_RECRUITER",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "JUNK_COLLECTOR",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "METAL_SEPARATOR",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "RUBBER_SHREDDER",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "CABLE_MAKER",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "BOILER",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        },
        {
            "id": 0,
            "type": "CIRCUIT_MAKER",
            "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
        }
    ]
}
```

This endpoint retrieves all factories.

### HTTP Request

`GET http://pawserver.it.itba.edu.ar/paw-2017a-4/api/v1/factories/all`

## Get a Specific Factory

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command returns JSON structured like this:

```json
{
    "id": 0,
    "type": "STOCK_INVESTOR",
    "recipe_url": "http://localhost:8080/api/v1/factories/0/recipe"
}
```

This endpoint retrieves a specific factory.

### HTTP Request

`GET http://localhost:8080/api/v1/factories/<factoryId>`

### URL Parameters

Parameter | Description
--------- | -----------
factoryId | The Id of the factory to retrieve

## Get a Factory's recipe

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command returns JSON structured like this:

```json
{
    "factory_id": 0,
    "factory_type": "STOCK_INVESTOR",
    "recipe": [
        {
            "id": 3,
            "name": "MONEY",
            "storage": 1000,
            "production": 1
        }
    ]
}
```

This endpoint retrieves a specific factory's recipe.

### HTTP Request

`GET http://localhost:8080/api/v1/factories/<factoryId>/recipe`

### URL Parameters

Parameter | Description
--------- | -----------
factoryId | The Id of the recipe's factory to retrieve


## Purchase Factories

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command does not return a body

This endpoint attempts to purhcase a factory with the 
### HTTP Request

`POST http://localhost:8080/api/v1/factories/purchase`

### Body Parameters

Parameter | Description
--------- | -----------
id | The factory's ID to be purchased
amount | The amount of factories desired to be purchased

### Errors
Code | Description
--------- | -----------
419 | User does not have either resources or production required to make the purchase.


## Upgrade a Factory

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command does not return a body

This endpoint attempts to upgrade an user's factory. 
### HTTP Request

`POST http://localhost:8080/api/v1/factories/purchase`

### Body Parameters

Parameter | Description
--------- | -----------
id | The factory's ID to be upgraded

### Errors
Code | Description
--------- | -----------
419 | User does not have the money required to upgrade the factory.

# Market

## Get the Market's prices

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command returns JSON structured like this:

```json
{
    "market": [
        {
            "id": 12,
            "name": "CIRCUITS",
            "price": 52
        },
        {
            "id": 11,
            "name": "CARDBOARD",
            "price": 1
        },
        {
            "id": 10,
            "name": "COPPER_CABLE",
            "price": 10
        },
        {
            "id": 9,
            "name": "COPPER",
            "price": 1
        },
        {
            "id": 8,
            "name": "METAL_SCRAP",
            "price": 12
        },
        {
            "id": 7,
            "name": "RUBBER",
            "price": 6
        },
        {
            "id": 6,
            "name": "TIRES",
            "price": 9
        },
        {
            "id": 5,
            "name": "IRON",
            "price": 6
        },
        {
            "id": 4,
            "name": "PEOPLE",
            "price": 10
        },
        {
            "id": 3,
            "name": "MONEY",
            "price": 10
        },
        {
            "id": 2,
            "name": "GOLD",
            "price": 21
        },
        {
            "id": 1,
            "name": "PLASTIC",
            "price": 42
        },
        {
            "id": 0,
            "name": "POWER",
            "price": 46
        }
    ]
}
```

This endpoint retrieves the maximun amount of factories of type <factoryID> which the user can buy with the current resources, and whether or no (and the cost) of buying 1, 10 and 100 of the given factory.
### HTTP Request

`GET http://localhost:8080/api/v1/market/prices`

## Purchase Resources

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command does not return a body

This endpoint attempts to upgrade an user's factory. 
### HTTP Request

`POST http://localhost:8080/api/v1/maket/purchase`

### Body Parameters

Parameter | Description
--------- | -----------
resource_type | The resourceType codeName to be purchased
amount | The amount of resources desired to be purchased


### Errors
Code | Description
--------- | -----------
419 | User does not have the money required to buy that amount of resources.

## Sell Resources

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command does not return a body

This endpoint attempts to upgrade an user's factory. 
### HTTP Request

`POST http://localhost:8080/api/v1/maket/sell`

### Body Parameters

Parameter | Description
--------- | -----------
resource_type | The resourceType codeName to be sold
amount | The amount of resources desired to be sold


### Errors
Code | Description
--------- | -----------
419 | User does not have that amount of resources to sell.

# Search

## Search for by user's o clan's name

```shell
curl "http://example.com/api/kittens/2"
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command returns JSON structured like this:

```json
{
    "results": [
        {
            "type": "user",
            "name": "wasabu",
            "url": "http://localhost:8080/api/v1/users/3"
        },
        {
            "type": "user",
            "name": "aaaa",
            "url": "http://localhost:8080/api/v1/users/2"
        },
        {
            "type": "clan",
            "name": "a",
            "url": "http://localhost:8080/api/v1/clans/13"
        },
        {
            "type": "clan",
            "name": "AAA",
            "url": "http://localhost:8080/api/v1/clans/21"
        }
    ]
}
```

This endpoint retrieves a list of users and clans which name contains the query parameter.

### HTTP Request

`GET http://localhost:8080/api/v1/search`

### Query Parameters

Parameter | Description
--------- | -----------
query | String included in the interested username o clan's name

