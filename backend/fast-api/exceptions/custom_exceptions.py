class CalculateFailedException(Exception):
    def __init__(self, message="Calculate stress index failed."):
        self.message = message
        super().__init__(self.message)
