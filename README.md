# Hostel & Mess Complaint and Feedback System

A command-line Java application for logging hostel/mess complaints, tracking
their resolution by an admin, automatically escalating complaints that go
unresolved for too long, and collecting student feedback once a complaint is
closed. Built for the **Programming in Java** course project (VITyarthi flipped
course evaluation).

## Overview

Students register, log in, and file complaints against categories such as
mess food quality, hygiene, room maintenance, electrical/plumbing issues,
Wi-Fi, and security. Every complaint is timestamped. An admin (warden) logs
into a separate dashboard to view complaints, update their status, and see
analytics. Any complaint that has been open longer than a configurable
threshold (default: **3 days**) is automatically flagged `ESCALATED` and
logged to an audit file the moment the admin dashboard loads (or on demand).
Once a complaint is marked `RESOLVED`, the student who filed it can leave a
1–5 star rating and comment.

## Features

- **Authentication** — student self-registration and login; a default admin
  account is auto-created on first run.
- **Complaint logging** — category, priority, and free-text description, with
  validation (empty/over-length descriptions are rejected).
- **Admin dashboard** — view all complaints, filter by status, update status
  with remarks, and trigger the escalation check manually.
- **Auto-escalation** — complaints still `PENDING`/`IN_PROGRESS` past the
  threshold are auto-flagged `ESCALATED` and written to `data/escalation.log`.
- **Feedback** — students rate (1–5) and comment on resolved complaints.
- **Analytics** — counts by status/category, average resolution time (hours),
  and current escalation count.
- **Persistence** — plain pipe-delimited text files under `data/` (no external
  database required), so the project runs anywhere the JDK runs.

## Technologies Used

- Java 17+ (standard library only — no external dependencies)
- Object-oriented design: abstraction (`User`), inheritance (`Student`,
  `Admin`), encapsulation, and a layered architecture (model / service / ui)
- `java.time` for timestamps and duration-based escalation logic
- File I/O (`java.nio.file`) for persistence

## Project Structure

```
hostel-mess-system/
├── src/com/hms/
│   ├── Main.java                  # entry point
│   ├── model/                     # User, Student, Admin, Complaint, Feedback, enums
│   ├── service/                   # AuthService, ComplaintService, FeedbackService,
│   │                              # ReportService, NotificationLogger
│   ├── ui/                        # AuthMenu, StudentMenu, AdminMenu (console views)
│   └── util/                      # FileStorageUtil, IdGenerator, InvalidInputException
├── data/                          # created automatically at first run (not in repo)
├── docs/                          # diagrams used in the project report
└── README.md
```

## Prerequisites

- **JDK 17 or later** installed and on your `PATH`.
  Check with:
  ```bash
  java -version
  javac -version
  ```
  If these commands aren't found, install a JDK first (e.g. `sudo apt install
  openjdk-21-jdk` on Ubuntu/Debian, or download from
  [adoptium.net](https://adoptium.net)).
- No other dependencies, build tools, or internet access are required — the
  project uses only the Java standard library.

## How to Set Up and Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/<KHUSH241>/<Hostel-Mess-System>.git
   cd <Hostel-Mess-System>
   ```

2. **Compile the project**
   ```bash
   javac -d bin $(find src -name "*.java")
   ```
   On Windows (PowerShell), use:
   ```powershell
   javac -d bin (Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName })
   ```

3. **Run the application**
   ```bash
   java -cp bin com.hms.Main
   ```

4. **First run**: the program creates a `data/` folder automatically and
   seeds a default admin account:
   - Username: `admin`
   - Password: `admin123`

   Register a student account from the main menu (option 2), then log back
   in as that student to file a complaint. Log in as `admin` to view/manage
   complaints, run the escalation check, and see analytics.

## Testing

There is no separate GUI or server to configure — every feature is reachable
from the console menus. A suggested manual test pass (also how this project
was validated before submission):

1. Register a student, log in, file a complaint.
2. Log in as `admin`, confirm the complaint appears under "View all
   complaints", move it to `IN_PROGRESS` then `RESOLVED` with remarks.
3. Log back in as the student and leave feedback (rating + comment) on the
   resolved complaint.
4. As admin, check "View analytics summary" — average resolution time and
   category counts should reflect the complaint just resolved.
5. To see escalation in action without waiting 3 real days, file a complaint,
   then manually edit its `createdAt` field in `data/complaints.txt` to a
   timestamp more than 3 days in the past, and reopen the admin dashboard —
   it will be auto-flagged `ESCALATED` and logged to `data/escalation.log`.

## Configuration

The escalation threshold is defined in one place —
`ComplaintService.ESCALATION_THRESHOLD_DAYS` — and can be changed to any
integer number of days.

## Notes / Known Limitations

- Passwords are stored in plain text in `data/users.txt`. This is acceptable
  for a course project scope but would need hashing (e.g. BCrypt) before any
  real-world use — noted here and in the project report's "Future
  Enhancements" section.
- Persistence uses flat files rather than a relational database to keep the
  project runnable from the command line with zero setup; the `docs/`
  folder includes an ER/schema diagram showing how the same data would map
  onto relational tables.
