from fastapi import APIRouter
from fastapi.responses import JSONResponse
from services.calculate_service import calc_stress_index
from exceptions.custom_exceptions import CalculateFailedException
from models.request_models import HeartRateRequest

router = APIRouter()

@router.post("/fast/stress/calc/single")
def calc_single_stress_index(data: HeartRateRequest):
    try:
        stress_index = calc_single_stress_index(data.hr_data)
        if stress_index is None:
            raise CalculateFailedException()
        content = {"code": "2000", "message": "SUCCESS", "data": {"stressIndex": stress_index}}
        return JSONResponse(content=content, status_code=200)
    except CalculateFailedException as e:
        error_content = {"code": "5000", "message": str(e), "data": None}
        return JSONResponse(content=error_content, status_code=500)
