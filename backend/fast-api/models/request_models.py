from pydantic import BaseModel

class HeartRateRequest(BaseModel):
    hr_data: list[int]
