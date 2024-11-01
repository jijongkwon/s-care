WINDOW_DIVISOR = 3
INF = 123456789

def find_healing_course_idx(stress_idx_arr: list[int]):
   size = len(stress_idx_arr)
   window_size = size // WINDOW_DIVISOR

   avg_arr = []

   start_idx, end_idx, min_avg = None, None, INF

   for i in range(size - window_size + 1):
      window = stress_idx_arr[i:i + window_size]
      avg = sum(window) / window_size
      avg_arr.append(avg)
      if avg < min_avg:
         min_avg = avg
         start_idx = i
         end_idx = i + window_size

   return avg_arr, start_idx, end_idx