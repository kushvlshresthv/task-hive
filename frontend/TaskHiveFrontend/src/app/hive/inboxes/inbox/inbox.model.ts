export interface Inbox {
  inboxId: string;
  initiator: string;
  title: 'INVITATION'| 'PROJECT_ACCEPTED' | 'PROJECT_REJECTED';
  projectName:string;
  pid: string;
  createdDate: Date;
}
