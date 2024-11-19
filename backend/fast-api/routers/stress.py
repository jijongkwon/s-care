from fastapi import APIRouter
from fastapi.responses import JSONResponse

from exceptions.custom_exceptions import CalculateFailedException
from models.request_models import SingleStressRequest, StressOverviewRequest
from models.response_models import StressOverview, SuccessResponse, ErrorResponse, StressData
from services.calculate_service import get_single_stress, get_multiple_stress_index
from services.stress_overview_service import find_healing_course_idx

router = APIRouter()


@router.post("/fast/stress/single")
def calc_single_stress_index(data: SingleStressRequest):
    try:
        stress = get_single_stress(data.hr_data)
        if stress is None:
            raise CalculateFailedException()
        response = StressData(stress=stress)
        return JSONResponse(content=SuccessResponse(data=response.model_dump()).model_dump(), status_code=200)
    except CalculateFailedException as e:
        return JSONResponse(content=ErrorResponse(code="5000", message=str(e)), status_code=500)


@router.post("/fast/stress/overview")
def calc_multiple_stress_index(data: StressOverviewRequest):
    try:
        stress_arr = get_multiple_stress_index(data.hr_data)
        if stress_arr is None:
            raise CalculateFailedException()
        healing_stress_avg, start_idx, end_idx = find_healing_course_idx(
            stress_arr) if data.walking_time >= 300 else (None, None, None)
        response = StressOverview(
            max_stress=max(stress_arr),
            min_stress=min(stress_arr),
            stress_indices=stress_arr,
            healing_stress_avg=healing_stress_avg,
            start_idx=start_idx,
            end_idx=end_idx
        )
        return JSONResponse(content=response.model_dump(), status_code=200)
    except CalculateFailedException as e:
        return JSONResponse(content=ErrorResponse(code="5000", message=str(e)), status_code=500)
