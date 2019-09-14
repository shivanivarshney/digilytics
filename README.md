# Digilytics

API Endpoints

> /register

Response

```
{
   "no_of_rows_parsed": "3",
   "no_of_rows_failed": "2",
   "error_file_url": "error_2019-46-1411:46:14"
}
```

Registers valid users and return error csv filepath in case of any errors

> /download/{filepath}

Response - returns error csv file