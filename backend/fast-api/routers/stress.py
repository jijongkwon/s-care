from fastapi import APIRouter
from fastapi.responses import JSONResponse
from services.calculate_service import get_single_stress_index, get_multiple_stress_index
from exceptions.custom_exceptions import CalculateFailedException
from models.request_models import HeartRateRequest

router = APIRouter()

@router.post("/fast/stress/calc/single")
def calc_single_stress_index(data: HeartRateRequest):
    try:
        stress_index = get_single_stress_index(data.hr_data)
        if stress_index is None:
            raise CalculateFailedException()
        content = {"code": "2000", "message": "SUCCESS", "data": {"stressIndex": stress_index}}
        return JSONResponse(content=content, status_code=200)
    except CalculateFailedException as e:
        error_content = {"code": "5000", "message": str(e), "data": None}
        return JSONResponse(content=error_content, status_code=500)

@router.post("/fast/stress/calc/multiple")
def calc_multiple_stress_index(data: HeartRateRequest):
    try:
        stress_index_arr = get_multiple_stress_index(data.hr_data)
        if stress_index_arr is None:
            raise CalculateFailedException()
        content = {"code": "2000", "message": "SUCCESS", "data": {"multipleStressIndex": stress_index_arr}}
        return JSONResponse(content=content, status_code=200)
    except CalculateFailedException as e:
        error_content = {"code": "5000", "message": str(e), "data": None}
        return JSONResponse(content=error_content, status_code=500)