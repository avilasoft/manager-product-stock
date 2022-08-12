import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import businessAssociate from 'app/entities/business-associate/business-associate.reducer';
// prettier-ignore
import unit from 'app/entities/unit/unit.reducer';
// prettier-ignore
import project from 'app/entities/project/project.reducer';
// prettier-ignore
import projectGroup from 'app/entities/project-group/project-group.reducer';
// prettier-ignore
import projectItemList from 'app/entities/project-item-list/project-item-list.reducer';
// prettier-ignore
import projectItemListItem from 'app/entities/project-item-list-item/project-item-list-item.reducer';
// prettier-ignore
import item from 'app/entities/item/item.reducer';
// prettier-ignore
import provider from 'app/entities/provider/provider.reducer';
// prettier-ignore
import providerItem from 'app/entities/provider-item/provider-item.reducer';
// prettier-ignore
import unitPriceList from 'app/entities/unit-price-list/unit-price-list.reducer';
// prettier-ignore
import unitPriceListItem from 'app/entities/unit-price-list-item/unit-price-list-item.reducer';
// prettier-ignore
import computedPriceList from 'app/entities/computed-price-list/computed-price-list.reducer';
// prettier-ignore
import computedPriceListItem from 'app/entities/computed-price-list-item/computed-price-list-item.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  businessAssociate,
  unit,
  project,
  projectGroup,
  projectItemList,
  projectItemListItem,
  item,
  provider,
  providerItem,
  unitPriceList,
  unitPriceListItem,
  computedPriceList,
  computedPriceListItem,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
