WINDOW_DIVISOR = 3
INF = 123456789


def find_healing_course_idx(stress_arr: list[int]):
    size = len(stress_arr)
    window_size = size // WINDOW_DIVISOR

    start_idx, end_idx, min_avg = None, None, 0

    for i in range(0, window_size):
        min_avg += (stress_arr[i] / window_size)

    for i in range(window_size, size - window_size + 1):
        tmp_avg = min_avg
        tmp_avg -= (stress_arr[i - window_size] / window_size)
        tmp_avg += (stress_arr[i] / window_size)
        if tmp_avg < min_avg:
            min_avg = tmp_avg
            start_idx = i
            end_idx = i + window_size - 1

    return min_avg, start_idx, end_idx
