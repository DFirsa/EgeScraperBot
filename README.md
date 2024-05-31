# EgeScrapperBot

![img](https://img.shields.io/badge/Kotlin-B125EA&style=for-the-badge&logo=kotlin&logoColor=white)
![img](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![img](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![img](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![img](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![img](https://img.shields.io/badge/Telegram-2CA5E0?style=for-the-badge&logo=telegram&logoColor=white)



## About
Telegram bot application created for automation Russian exam results fetching.
Bot is fetching results only for Saint-Petersburg region using [special resource](https://www.ege.spb.ru).

## Available features:
- Add students data for fetching (using text or excel-file)
- Remove provided students data
- Remove all information about chat with bot
- Fetching results by scheduled task
- Sending notification about new results
- Seeing available results

## How to run

```bash
docker --env-file .env up
```

Environment file should look like:
```env
# postgres configuration environment
PG_DB_NAME=
PG_USR=
PG_PWD=
PG_PORT=

# application environment
APP_PORT=
TELEGRAM_TOKEN=
```

Hint for running from Russian IP-address: [link](https://huecker.io)

## Used technologies
- Kotlin
- Gradle
- JOOQ
- PostgreSQL
- Telegram-bot-api
- Spring boot
- Docker


