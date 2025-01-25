export interface Project {
  pid: number | null;
  projectName: string | null;
  projectDescription?: string | null;
  startDate: string | null;
  finishDate: string | null;
  priority: string | null;
  projectType: string | null;
}
