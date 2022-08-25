import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './unit.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const unitEntity = useAppSelector(state => state.unit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unitDetailsHeading">
          <Translate contentKey="managerProductStockApp.unit.detail.title">Unit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{unitEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.unit.name">Name</Translate>
            </span>
          </dt>
          <dd>{unitEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.unit.description">Description</Translate>
            </span>
          </dt>
          <dd>{unitEntity.description}</dd>
          <dt>
            <span id="symbol">
              <Translate contentKey="managerProductStockApp.unit.symbol">Symbol</Translate>
            </span>
          </dt>
          <dd>{unitEntity.symbol}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="managerProductStockApp.unit.type">Type</Translate>
            </span>
          </dt>
          <dd>{unitEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/unit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unit/${unitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnitDetail;
