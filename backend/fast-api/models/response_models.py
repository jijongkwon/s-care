from typing import Union, List

from pydantic import BaseModel


class SuccessResponse(BaseModel):
    code: str = "2000"
    message: str = "SUCCESS"
    data: Union[dict, None]


class ErrorResponse(BaseModel):
    code: str
    message: str
    data: None


class StressData(BaseModel):
    stress: float


class StressOverview(BaseModel):
    max_stress: float
    min_stress: float
    healing_stress_avg: float
    start_idx: Union[int, None]
    end_idx: Union[int, None]
    stress_indices: List[float]
