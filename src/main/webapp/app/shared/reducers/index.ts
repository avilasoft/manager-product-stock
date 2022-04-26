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
import provider from 'app/entities/provider/provider.reducer';
// prettier-ignore
import unitType from 'app/entities/unit-type/unit-type.reducer';
// prettier-ignore
import unit from 'app/entities/unit/unit.reducer';
// prettier-ignore
import category from 'app/entities/category/category.reducer';
// prettier-ignore
import batchStatus from 'app/entities/batch-status/batch-status.reducer';
// prettier-ignore
import batch from 'app/entities/batch/batch.reducer';
// prettier-ignore
import providerProduct from 'app/entities/provider-product/provider-product.reducer';
// prettier-ignore
import providerProductPrice from 'app/entities/provider-product-price/provider-product-price.reducer';
// prettier-ignore
import batchProduct from 'app/entities/batch-product/batch-product.reducer';
// prettier-ignore
import productCategory from 'app/entities/product-category/product-category.reducer';
// prettier-ignore
import price from 'app/entities/price/price.reducer';
// prettier-ignore
import priceHistory from 'app/entities/price-history/price-history.reducer';
// prettier-ignore
import unitEquivalence from 'app/entities/unit-equivalence/unit-equivalence.reducer';
// prettier-ignore
import product from 'app/entities/product/product.reducer';
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
  provider,
  unitType,
  unit,
  category,
  batchStatus,
  batch,
  providerProduct,
  providerProductPrice,
  batchProduct,
  productCategory,
  price,
  priceHistory,
  unitEquivalence,
  product,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
