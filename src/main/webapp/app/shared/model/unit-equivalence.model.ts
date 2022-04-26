import { IUnit } from 'app/shared/model/unit.model';

export interface IUnitEquivalence {
  id?: number;
  name?: string;
  description?: string | null;
  parents?: IUnitEquivalence[] | null;
  unit?: IUnit;
  child?: IUnitEquivalence | null;
}

export const defaultValue: Readonly<IUnitEquivalence> = {};
