PUT http://localhost:9200/demo

```json
{
  "mappings": {
    "properties": {
      "demoId": {
        "type": "long"
      },
      "demoName": {
        "type": "text"
      },
      "createTime": {
        "type": "date",
        "format": "yyyy-MM-dd HH:mm:ss"
      }
    }
  }
}
```