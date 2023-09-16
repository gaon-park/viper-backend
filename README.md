# viper-backed

- 메이플스토리 SOAP API를 사용한 대표캐릭터의 레벨업 예측기
- 고객센터 문의 결과 상업적 목적의 사용이 허가되지 않은 api였기 때문에 **상용화 금지!**

# 목차
1. [기술 스택](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)
2. [블로그 기록](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EB%B8%94%EB%A1%9C%EA%B7%B8-%EA%B8%B0%EB%A1%9D)
3. [REST API](https://github.com/gaon-park/viper-backend/blob/master/README.md#rest-api)
    1. [유저 신규 등록 API](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EC%9C%A0%EC%A0%80-%EC%8B%A0%EA%B7%9C-%EB%93%B1%EB%A1%9D-api)
    2. [로그인 API](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EB%A1%9C%EA%B7%B8%EC%9D%B8-api)
    3. [캐릭터 정보 요청 API](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EC%BA%90%EB%A6%AD%ED%84%B0-%EC%A0%95%EB%B3%B4-%EC%9A%94%EC%B2%AD-api)
    4. [배치 수동 실행](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EB%B0%B0%EC%B9%98-%EC%88%98%EB%8F%99-%EC%8B%A4%ED%96%89)
    5. [경험치 분석 API](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EA%B2%BD%ED%97%98%EC%B9%98-%EB%B6%84%EC%84%9D-api)
    6. [데이터 이력 API](https://github.com/gaon-park/viper-backend/blob/master/README.md#%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%9D%B4%EB%A0%A5-api)

# 기술 스택
<table>
    <thead>
        <tr>
            <th>분류</th>
            <th>기술</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Language</td>
            <td>Kotlin</td>
        </tr>
        <tr>
            <td>Framework</td>
            <td>SpringBoot</td>
        </tr>
        <tr>
            <td>DB</td>
            <td>MySQL</td>
        </tr>
    </tbody>
</table>


# 블로그 기록

1. **[메이플스토리 API를 이용하여 대표캐릭터 정보를 불러오는 API](https://ondol-diary.tistory.com/3)**
2. **[Jsoup를 이용한 캐릭터 정보 탐색](https://ondol-diary.tistory.com/4)**
3. **[Json PropertyNamingStrategy](https://ondol-diary.tistory.com/5)**

# REST API

### 유저 신규 등록 API
- `post: /regist`
- 유저의 경험치 정보를 “스케줄링 된 배치 프로그램”을 이용하여 정기적으로 수집하려는 목적

<details>
<summary>request</summary>
<div markdown="1">

```json
{
    "accountId": "넥슨 홈페이지에서 알 수 있는 계정 ID", // 확인 방법은 블로그 기록 참고
    "email": "dol@gmail.com", // 이메일 ID 
    "password": "qwer", // 비밀번호
    "confirmPassword": "qwer", // 비밀번호 확인
    "terms": true // 정보 수집 동의
}
```

</div>
</details>

<details>
<summary>response</summary>
<div markdown="1">

```json
{
    "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2xAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY5NDg2NzM4NSwiZXhwIjoxNjk0ODY5MTg1fQ.D1Qj12Vqc1jeOoXb2YJ5A8sjd_AHzqCBQREg2NOtRw8"
}
```

</div>

</details>


### 로그인 API
- `post: localhost:8080/login`
- 유효한 토큰을 발급
<details>
<summary>request</summary>
<div markdown="1">

```json
{
    "email": "dol@gmail.com", // 이메일 ID
    "password": "qwer" // 비밀번호
}
```

</div>
</details>

<details>
<summary>response</summary>
<div markdown="1">

```json
{
    "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2xAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY5NDg2NzM4NSwiZXhwIjoxNjk0ODY5MTg1fQ.D1Qj12Vqc1jeOoXb2YJ5A8sjd_AHzqCBQREg2NOtRw8"
}
```

</div>
</details>

### 캐릭터 정보 요청 API 
- `get: localhost:8080/character?userId=1`

<details>
<summary>request</summary>

<div markdown="1">

- parameter: userId (DB 등록된 유저의 pk)

</div>

</details>

<details>
<summary>response</summary>
<div markdown="1">

```json
{
    "avatar_img_url": "http://avatar.maplestory.nexon.com/Character/BMEALBGIOAHHIICCMOLCFNFKFLBLFMKPNGPBLMCKIGGHEFMKJOJOBHCFBCJNAAIELNHCBHBMFLBGONPBNDBIOEPKGPCABLDLCODBOLBGPHKAPPFCKNGBMBIPCEAFNIEKDKIBOGNIPAALJBBAJHBDELMNHAINLFLLOINKFILNENLPKCBILDIOHFJEMNAOBNFHKMEPPEHKPAMDMJAJPACFPMGGMCBFFLELJLGPDJNMHOFHPFGCBLDCIOECPFICDDKM.png",
	// 아바타 이미지 cdn link
    "world_name": "리부트", // 월드
    "character_name": "뽀돌", // 캐릭터 닉네임
    "lev": 278, // 캐릭터 레벨
    "exp": 3791080069971, // 캐릭터 보유 경험치
    "job": "해적", // 캐릭터 직업군
    "job_detail": "바이퍼", // 캐릭터 세부 직업
    "pop": 56, // 캐릭터 인기도
    "total_rank": 14006, // 캐릭터 전체 월드 랭킹
    "world_rank": 10324 // 캐릭터 월드 랭킹
}
```

</div>

</details>

### 배치 수동 실행
- `put: localhost:8080/batch/data`
- 캐릭터 정보 자동 수집 배치 프로그램 수동 실행 API
- 실행 날짜로 수집된 데이터를 일괄 삭제하고 재수집

<details>
<summary>response</summary>
<div markdown="1">

```
ok
```

</div>
</details>

### 경험치 분석 API
- `get: localhost:8080/analyze/exp`
- 지정 기간동안 수집된 데이터를 바탕으로 경험치 상승폭을 분석
- 지정 기간의 페이스로 사냥을 지속했을 때 목표 레벨에 도달할 수 있는 날짜를 예측

<details>
<summary>request</summary>
<div markdown="1">

```json
{
    "userId": 1, // DB 등록된 유저의 pk
    "startDate": "2023-09-15", // 페이스 분석 시작 일자
    "endDate": "2023-09-16", // 페이스 분석 마지막 일자
    "targetLev": 280 // 달성하고자 하는 레벨
}

```

</div>
</details>

<details>
<summary>response</summary>
<div markdown="1">

> error 응답

- 수집된 데이터로 페이스(상승폭)를 분석할 수 없는 경우

```json
{
    "error": true,
    "message": "경험치 데이터가 쌓이지 않아 계산이 불가합니다."
}

```

> 정상 응답

```json
{
    "total_duration": 1, // 페이스 분석에 사용된 기간의 길이
    "avg_exp": 14080000000, // 평균 경험치 상승치
    "avg_exp_percent": 0, // 평균 경험치 상승치(%)
    "target_lev": 280, // 목표 레벨
    "remain_exp_for_target_lev": 33869708042158, // 목표 레벨까지 남은 경험치
    "remain_days_for_target_lev": 2406, // 목표 레벨까지 남은 기간의 길이
    "exp_percent_for_target_lev": 79.42785723000222, // 목표 레벨까지 남은 경험치(%)
    "completion_date": "2030-04-18" // 달성 일자 (예측값)
}
```
```json
{
    "total_duration": 1,
    "avg_exp": 14080000000,
    "avg_exp_percent": 0,
    "target_lev": 290,
    "remain_exp_for_target_lev": 996979461309219,
    "remain_days_for_target_lev": 70808,
    "exp_percent_for_target_lev": 11.59558144026126,
    "completion_date": "2217-07-29"
}
```
```json
{
    "total_duration": 1,
    "avg_exp": 14080000000,
    "avg_exp_percent": 0,
    "target_lev": 300,
    "remain_exp_for_target_lev": 9970006377507102,
    "remain_days_for_target_lev": 708097,
    "exp_percent_for_target_lev": 1.2946430879619548,
    "completion_date": "3962-05-31"
}
```

</div>
</details>

### 데이터 이력 API
- `get: localhost:8080/character/history`
- 수집된 데이터 이력 요청 API

<details>
<summary>request</summary>
<div markdown="1">

```json
{
    "userId": 1 // DB 등록된 유저의 pk
}
```

</div>
</details>

<details>
<summary>response</summary>
<div markdown="1">

```json
{
    "expHistory": [
        {
            "lev": 278,
            "exp": 3804080069971,
            "target_lev": 300,
            "exp_percent_for_next_lev": 21.204578958618477,
            "exp_percent_for_target_lev": 1.2946430879619548,
            "date": "2023-09-16"
        }
    ],
    "worldRankHistory": [
        {
            "rank": 10324,
            "date": "2023-09-16"
        }
    ],
    "totalRankHistory": [
        {
            "rank": 14006,
            "date": "2023-09-16"
        }
    ],
    "popHistory": [
        {
            "pop": 56,
            "date": "2023-09-16"
        }
    ]
}
```

</div>
</details>
