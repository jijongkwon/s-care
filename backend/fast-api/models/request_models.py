from pydantic import BaseModel


class SingleStressRequest(BaseModel):
    hr_data: list[int]


class StressOverviewRequest(BaseModel):
    hr_data: list[float]
    walking_time: int
