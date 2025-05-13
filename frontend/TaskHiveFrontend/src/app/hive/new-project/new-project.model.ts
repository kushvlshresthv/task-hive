
export class Project {
  pid: number | null;
  projectName: string | null;
  projectDescription?: string | null;
  startDate: string | null;
  finishDate: string | null;
  priority: string | null;
  projectType: string | null;
  status: 'COMPLETED'|'ONHOLD'|'FAILED'|'IN_PROGRESS' | 'PLANNED' | 'CANCELLED' | 'OVERDUE' | 'REVIEWED' | null;

  constructor() {
    this.pid = null;
    this.projectName=null;
    this.projectDescription = null;
    this.startDate = null;
    this.finishDate = null;
    this.priority = null;
    this.projectType = null;
    this.status = null
  }
}
