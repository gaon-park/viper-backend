# Level Up Pace Calculator (2022.10 ~ 2022.11)

- ゲーム会社の非公式SOAP APIを活用し、登録されている代表キャラクターの情報を取得
- ユーザの経験値上昇ペースを分析し、目標レベルまでの到達日を計算
- 詳細内容 repo: https://github.com/gaon-park/viper-backend

## Index
1. 技術
2. Blog 記録
3. REST API
    1. ユーザ新規要録 API
    2. ログイン API
    3. キャラクター情報要請 API
    4. バッチ手動実行
    5. 経験値分析 API
    6. データ履歴 API

## 技術

| 分類        | 技術         |
|-----------|------------|
| Language  | Kotlin     |
| Framework | SpringBoot |
| DB        | MySQL      |

## Blog 記録

1. **[メープルストーリーAPIを使って代表キャラクター情報をとってくるAPI](https://ondol-diary.tistory.com/3)**
2. **[Jsoupを使ったキャラクター情報探索](https://ondol-diary.tistory.com/4)**
3. **[Json PropertyNamingStrategy](https://ondol-diary.tistory.com/5)**

## REST API

#### ユーザ新規登録 API
- `post: /regist`
- ユーザの経験値情報を「スケジューリングされてるバッチプログラム」を使って定期的に取得する

> request

```json
{
    "accountId": "NEXON HPで確認できるアカウント ID", 
    "email": "dol@gmail.com", // メール
    "password": "qwer", // パスワード
    "confirmPassword": "qwer", // パスワード確認
    "terms": true // 情報収集同義
}
```

> response

```json
{
    "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2xAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY5NDg2NzM4NSwiZXhwIjoxNjk0ODY5MTg1fQ.D1Qj12Vqc1jeOoXb2YJ5A8sjd_AHzqCBQREg2NOtRw8"
}
```

#### ログイン API
- `post: /login`
- 有効なトークンを発給

> request

```json
{
    "email": "dol@gmail.com", // メール
    "password": "qwer" // パスワード
}
```

> response

```json
{
    "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2xAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY5NDg2NzM4NSwiZXhwIjoxNjk0ODY5MTg1fQ.D1Qj12Vqc1jeOoXb2YJ5A8sjd_AHzqCBQREg2NOtRw8"
}
```

#### キャラクター情報要請 API 
- `get: /character?userId=1`

> request

- parameter: userId (DBに登録されてるユーザのPK)

> response

```json
{
    "avatar_img_url": "http://avatar.maplestory.nexon.com/Character/BMEALBGIOAHHIICCMOLCFNFKFLBLFMKPNGPBLMCKIGGHEFMKJOJOBHCFBCJNAAIELNHCBHBMFLBGONPBNDBIOEPKGPCABLDLCODBOLBGPHKAPPFCKNGBMBIPCEAFNIEKDKIBOGNIPAALJBBAJHBDELMNHAINLFLLOINKFILNENLPKCBILDIOHFJEMNAOBNFHKMEPPEHKPAMDMJAJPACFPMGGMCBFFLELJLGPDJNMHOFHPFGCBLDCIOECPFICDDKM.png",
	// キャラクターイメージ cdn link
    "world_name": "리부트", // ワルド（サーバ）
    "character_name": "뽀돌", // ニックネーム
    "lev": 278, // レベル
    "exp": 3791080069971, // 保有経験値
    "job": "해적", // 職業
    "job_detail": "바이퍼", // 詳細職業
    "pop": 56, // 人気度
    "total_rank": 14006, // 全体ワルドランキング
    "world_rank": 10324 // キャラクター所属ワルドランキング
}
```

#### バッチ手動実行
- `put: /batch/data`
- キャラクター情報を自動収集するバッチプログラムの手動実行 API
- 実行日で収集したデータを一括作上司、再収集

> request

```
ok
```

#### 経験値分析 API
- `get: /analyze/exp`
- 指定期間中に収集したデータ基盤に経験値上昇幅を分析
- 指定期間中のペースで続く場合、目標レベルに到達できる日を計算

> request

```json
{
    "userId": 1, // DB 登録されたユーザのPK
    "startDate": "2023-09-15", // ペース分析開始日
    "endDate": "2023-09-16", // ペース分析終了日
    "targetLev": 280 // 目標レベル
}

```

> error response

- 収集されたデータでペース分析できない場合

```json
{
    "error": true,
    "message": "経験値データが足りなく、計算できませんでした。"
}

```

> success response

```json
{
    "total_duration": 1, // ペース分析に使った日々数
    "avg_exp": 14080000000, // 平均経験値の上昇値
    "avg_exp_percent": 0, // 平均経験値の上昇値(%)
    "target_lev": 280, // 目標レベル
    "remain_exp_for_target_lev": 33869708042158, // 目標レベルまで残った経験値
    "remain_days_for_target_lev": 2406, // 目標レベルまで残った期間
    "exp_percent_for_target_lev": 79.42785723000222, // 目標レベルまで残った経験値(%)
    "completion_date": "2030-04-18" // 達成日 (予測値)
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

#### データ履歴 API
- `get: /character/history`
- 収集したデータから履歴要請 API

> request

```json
{
    "userId": 1 // DB 登録されたユーザのPK
}
```

> response

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
