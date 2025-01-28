## ScheduleAppK (ИКТИБ Расписание)
Приложение, которое получает расписание учебных групп ИКТИБа

## Скриншоты
<div>
<img src="docs/enter.png" width="30%" />
<img src="docs/home.png" width="30%" />
<img src="docs/home_few.png" width="30%" />
<img src="docs/home_current.png" width="30%" />
<img src="docs/drawer.png" width="30%" />
<img src="docs/settings.png" width="30%" />
<img src="docs/groups_add.png" width="30%" />
</div>

## Библиотеки и зависимости
- Retrofit
- Room
- RxKotlin
- Hilt
- MVVM
- View based

## Модульная архитектура

   ```markdown
   core
   ├── data
   ├── database
   ├── domain
   ├── models
   ├── network
   ├── sharpref
   ├── utils
   ├── values
   ├── views
   features
   ├── enter
   ├── schedule
   ├── settings
