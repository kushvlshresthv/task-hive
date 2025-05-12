//popupBy property is for styling based on what created the popup(error or success)
export class Popup {
  state:boolean = false;
  message: string = "" ;
  popupBy?: string = "";
  popupActiveTime: number = 0;

  activatePopup(message: string, popup_by: string, popupActiveTimeInMs = 2000  ): void {
    this.state = true;
    this.message = message;
    this.popupBy = popup_by;  //for styling purposes
    this.popupActiveTime = popupActiveTimeInMs;

    setTimeout(()=> {
      this.state = false;
    }, popupActiveTimeInMs)
  }
}
