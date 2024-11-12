from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from datetime import datetime
from typing import List, Dict
from collections import Counter
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()
# Allow all origins for testing (you should refine this for production)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # You can specify the IP of your mobile device or localhost for production
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
# Define data structure for activity
class UserActivity(BaseModel):
    user_id: int
    action: str  # e.g., "login", "view", "purchase"
    timestamp: datetime

# Store activities in a simple list (you can use a database for production)
activities_log: List[UserActivity] = []

# Endpoint to log user activity
@app.post("/log_activity")
async def log_activity(activity: UserActivity):
    activities_log.append(activity)
    return {"status": "Activity logged"}

# Endpoint to get all logged activities
@app.get("/activities")
async def get_activities():
    return activities_log

# Endpoint to get analytics summary (e.g., most common actions)
@app.get("/analytics")
async def get_analytics():
    # Get the count of each action type
    action_counter = Counter(activity.action for activity in activities_log)
    most_common_actions = action_counter.most_common(5)  # Top 5 actions

    # Active users count
    unique_users = len(set(activity.user_id for activity in activities_log))

    return {
        "total_activities": len(activities_log),
        "unique_users": unique_users,
        "most_common_actions": most_common_actions,
    }
