import { NavLink, Route, Routes } from 'react-router-dom';
import { AppBar, Box, Button, Container, CssBaseline, Toolbar, Typography } from '@mui/material';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import InventoryIcon from '@mui/icons-material/Inventory';
import ListAltIcon from '@mui/icons-material/ListAlt';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { AccountsPage } from './pages/AccountsPage';
import { SecuritiesPage } from './pages/SecuritiesPage';
import { OrdersPage } from './pages/OrdersPage';
import { CreateOrderPage } from './pages/CreateOrderPage';

const navItems = [
  { to: '/', label: 'Orders', icon: <ListAltIcon fontSize="small" /> },
  { to: '/create-order', label: 'Create Order', icon: <AddCircleIcon fontSize="small" /> },
  { to: '/accounts', label: 'Accounts', icon: <AccountBalanceIcon fontSize="small" /> },
  { to: '/securities', label: 'Securities', icon: <InventoryIcon fontSize="small" /> },
];

export function App() {
  return (
    <>
      <CssBaseline />
      <AppBar position="static" color="default" elevation={0} sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Toolbar sx={{ gap: 2, flexWrap: 'wrap' }}>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            CRD Practice
          </Typography>
          {navItems.map((item) => (
            <Button
              key={item.to}
              component={NavLink}
              to={item.to}
              startIcon={item.icon}
              sx={{
                color: 'text.primary',
                '&.active': {
                  bgcolor: 'primary.main',
                  color: 'primary.contrastText',
                },
              }}
            >
              {item.label}
            </Button>
          ))}
        </Toolbar>
      </AppBar>
      <Box component="main" sx={{ bgcolor: '#f6f8fb', minHeight: 'calc(100vh - 64px)', py: 4 }}>
        <Container maxWidth="lg">
          <Routes>
            <Route path="/" element={<OrdersPage />} />
            <Route path="/create-order" element={<CreateOrderPage />} />
            <Route path="/accounts" element={<AccountsPage />} />
            <Route path="/securities" element={<SecuritiesPage />} />
          </Routes>
        </Container>
      </Box>
    </>
  );
}
