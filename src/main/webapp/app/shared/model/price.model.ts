import { IUnit } from 'app/shared/model/unit.model';

export interface IPrice {
  id?: number;
  name?: string;
  description?: string | null;
  unit?: IUnit;
}

export const defaultValue: Readonly<IPrice> = {};
