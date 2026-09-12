# Problem Statement

## Problem Statement

College hostel and mess complaints are commonly handled informally — verbal
reports to a warden, WhatsApp messages, or a physical register — which makes
it hard to track how long an issue has been open, who is responsible for
resolving it, and whether it was ever actually fixed. Complaints about
recurring problems (e.g. poor mess hygiene, electrical faults) can go
unresolved for days without anyone being alerted, and there is no structured
way for students to give feedback on how an issue was handled.

The **Hostel & Mess Complaint and Feedback System** addresses this by giving
students a simple way to log complaints against defined categories, giving
hostel administration a dashboard to track and resolve them, and — critically
— automatically escalating any complaint that stays unresolved past a set
number of days, so nothing silently falls through the cracks.

## Scope of the Project

In scope:
- Student registration/login and admin login (single default admin account,
  seeded automatically).
- Filing complaints with category, priority, and description.
- Admin view of all complaints, filterable by status, with status updates and
  remarks.
- Automatic escalation of complaints unresolved beyond a configurable
  threshold (default 3 days), with an audit log of escalation events.
- Post-resolution feedback (1–5 rating + comment) from the student who filed
  the complaint.
- Basic analytics for admins: counts by status/category, average resolution
  time, and current escalation count.
- Command-line interface, file-based persistence — runnable with just a JDK,
  no external database or server setup.

Out of scope (see the report's "Future Enhancements" section):
- Multi-admin role hierarchy / department-based routing.
- Email or SMS notifications (escalation is currently logged to a file, not
  sent externally).
- A graphical or web front end.
- Password hashing/encryption (flagged as a known limitation for this
  course-project scope).

## Target Users

- **Students** living in the hostel, who need to report mess/hostel issues
  and track their resolution.
- **Hostel administration / warden**, who needs a consolidated view of open
  complaints, a way to prioritize and act on them, and visibility into which
  complaints are overdue.

## High-Level Features

1. **Authentication** — student self-registration, student & admin login.
2. **Complaint Logging** — category + priority + description, validated
   input, timestamped on creation.
3. **Admin Dashboard** — view all/filtered complaints, update status with
   remarks, trigger escalation checks, view analytics.
4. **Auto-Escalation** — complaints open beyond the threshold are
   automatically flagged and logged, without any manual admin action.
5. **Feedback Collection** — students rate and comment on resolved
   complaints, feeding into the analytics.
