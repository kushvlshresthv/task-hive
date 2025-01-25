import { AbstractControl } from '@angular/forms';

export function isFinishDateLaterThanStartDate(control: AbstractControl) {
  const startDate = control.get('startDate')?.value;
  const finishDate = control.get('finishDate')?.value;
  const startDateObj = new Date(startDate);
  const finishDateObj = new Date(finishDate);
  if (startDateObj.getTime() > finishDateObj.getTime()) {
    return {
      improperDates: 'start date is greater than finish date',
    };
  }

  return null;
}

export function isFuture(control: AbstractControl) {
  const selectedDate = new Date(control.value);

  if (
    Date.now() > selectedDate.getTime() &&
    Date.now() - selectedDate.getTime() > 86400000 //greater than 1 day
  ) {
    return {
      notFutureDate: 'select a future date',
    };
  }
  return null;
}
