import { IUnit } from 'app/shared/model/unit.model';

export interface IUnitType {
  id?: number;
  name?: string;
  description?: string | null;
  units?: IUnit[];
}

export const defaultValue: Readonly<IUnitType> = {};
