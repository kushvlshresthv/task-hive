//popupBy property is for styling based on what created the popup(error or success)
export interface Popup {
  state: boolean;
  message: string;
  popupBy?: string;
  popupActiveTime: number;
}

//activates a popup for a particular time based on the message, what caused the popup and the popup active time.

export function activatePopup(
  message: string,
  popup_by: string,
  popupActiveTimeInMs = 2000, //default popup active time
): Popup {
  const popup = {
    state: true,
    message: message,
    popupBy: popup_by,
    popupActiveTime: popupActiveTimeInMs,
  };

  setTimeout(() => {
    popup.state = false;
  }, popupActiveTimeInMs);

  return popup;
}

export function initializePopup(): Popup {
  return {
    state: false,
    message: '',
    popupBy: '',
    popupActiveTime: 0,
  };
}
