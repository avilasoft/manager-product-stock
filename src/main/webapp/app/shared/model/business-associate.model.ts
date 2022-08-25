import { IUser } from 'app/shared/model/user.model';
import { IProvider } from 'app/shared/model/provider.model';
import { IProject } from 'app/shared/model/project.model';
import { BusinessAssociateType } from 'app/shared/model/enumerations/business-associate-type.model';

export interface IBusinessAssociate {
  id?: number;
  comercialName?: string;
  description?: string | null;
  type?: BusinessAssociateType;
  user?: IUser;
  providers?: IProvider[] | null;
  projects?: IProject[] | null;
}

export const defaultValue: Readonly<IBusinessAssociate> = {};
